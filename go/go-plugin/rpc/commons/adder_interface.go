package example

import (
	"fmt"
	"net/rpc"

	plugin "github.com/hashicorp/go-plugin"
)

// Adder is the interface that we're exposing as a plugin.
type Adder interface {
	Add(x, y int) (int, error)
}

// Here is an implementation that talks over RPC
type AdderRPC struct{ client *rpc.Client }

func (g *AdderRPC) Add(x, y int) (int, error) {
	var resp int
	var req rpc.Server.Args
	err := g.client.Call("Plugin.Greet", x, y, &resp)
	if err != nil {
		// You usually want your interfaces to return errors. If they don't,
		// there isn't much other choice here.
		// panic(err)
		return fmt.Errorf("failed on rpc call Plugin.Add: %v", err)
	}

	return resp, nil
}

// Here is the RPC server that GreeterRPC talks to, conforming to
// the requirements of net/rpc
type GreeterRPCServer struct {
	// This is the real implementation
	Impl Greeter
}

func (s *GreeterRPCServer) Greet(args interface{}, resp *string) error {
	*resp = s.Impl.Greet()
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
type GreeterPlugin struct {
	// Impl Injection
	Impl Greeter
}

func (p *GreeterPlugin) Server(*plugin.MuxBroker) (interface{}, error) {
	return &GreeterRPCServer{Impl: p.Impl}, nil
}

func (GreeterPlugin) Client(b *plugin.MuxBroker, c *rpc.Client) (interface{}, error) {
	return &GreeterRPC{client: c}, nil
}