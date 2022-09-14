package main

import (
	"os"

	"github.com/hashicorp/go-hclog"
	plugin "github.com/hashicorp/go-plugin"
	commons "github.com/unknowntpo/playground-2022/go/go-plugin/rpc/commons"
)

// Here is a real implementation of Greeter
type AdderImpl struct {
	logger hclog.Logger
}

func (g *AdderImpl) Add(args commons.AddArgs, resp *int) error {
	g.logger.Debug("message from AdderImpl.Add by unknowntpo")
	return nil
}

// handshakeConfigs are used to just do a basic handshake between
// a plugin and host. If the handshake fails, a user friendly error is shown.
// This prevents users from executing bad plugins or executing a plugin
// directory. It is a UX feature, not a security feature.
var handshakeConfig = plugin.HandshakeConfig{
	ProtocolVersion:  1,
	MagicCookieKey:   "basic_plugin",
	MagicCookieValue: "hello",
}

func main() {
	logger := hclog.New(&hclog.LoggerOptions{
		Level:      hclog.Trace,
		Output:     os.Stderr,
		JSONFormat: true,
	})

	adder := &AdderImpl{
		logger: logger,
	}
	// pluginMap is the map of plugins we can dispense.
	var pluginMap = map[string]plugin.Plugin{
		"adder": &commons.AdderPlugin{Impl: adder},
	}

	logger.Debug("message from adder plugin", "foo", "bar")

	plugin.Serve(&plugin.ServeConfig{
		HandshakeConfig: handshakeConfig,
		Plugins:         pluginMap,
	})
}
