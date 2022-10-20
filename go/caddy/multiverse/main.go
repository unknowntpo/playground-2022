package main

import (
	caddycmd "github.com/caddyserver/caddy/v2/cmd"

	// plug in Caddy modules here
	_ "github.com/caddyserver/caddy/v2/modules/standard"
	_ "github.com/unknowntpo/playground-2022/go/caddy/multiverse/calculator"
	_ "github.com/unknowntpo/playground-2022/go/caddy/multiverse/helloworld"
)

func main() {
	caddycmd.Main()
}
