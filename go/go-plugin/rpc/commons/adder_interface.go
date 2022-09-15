package example

import (
	"fmt"
	"net/rpc"

	plugin "github.com/hashicorp/go-plugin"
)

// Adder is the interface that we're exposing as a plugin.
type Adder interface {
	Add(AddArgs, *int) error
}

type AddArgs struct {
	A, B int
}

// Here is an implementation that talks over RPC
type AdderRPC struct{ client *rpc.Client }

func (g *AdderRPC) Add(args AddArgs, reply *int) error {
	var resp int
	err := g.client.Call("Plugin.Add", args, &resp)
	if err != nil {
		// You usually want your interfaces to return errors. If they don't,
		// there isn't much other choice here.
		// panic(err)
		return fmt.Errorf("failed on rpc call Plugin.Add: %v", err)
	}
	return nil
}

// Here is the RPC server that GreeterRPC talks to, conforming to
// the requirements of net/rpc
type AdderRPCServer struct {
	// This is the real implementation
	Impl Adder
}

func (s *AdderRPCServer) Add(args AddArgs, resp *int) error {
	if err := s.Impl.Add(args, resp); err != nil {
		return fmt.Errorf("failed on s.Impl.Add: %v", err)
	}
	return nil
}

// This is the implementation of plugin.Plugin so we can serve/consume this
//
// This has two methods: Server must return an RPC server for this plugin
// type. We construct a GreeterRPCServer for this.
//
// Client must return an implementation of our interface that communicates
// over an RPC client. We return GreeterRPC for this.
//
// Ignore MuxBroker. That is used to create more multiplexed streams on our
// plugin connection and is a more advanced use case.
type AdderPlugin struct {
	// Impl Injection
	Impl Adder
}

func (p *AdderPlugin) Server(*plugin.MuxBroker) (interface{}, error) {
	return &AdderRPCServer{Impl: p.Impl}, nil
}

func (AdderPlugin) Client(b *plugin.MuxBroker, c *rpc.Client) (interface{}, error) {
	return &AdderRPC{client: c}, nil
}
