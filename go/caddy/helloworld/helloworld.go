package helloworld

import (
	"errors"
	"net/http"

	caddy "github.com/caddyserver/caddy/v2"
	"github.com/caddyserver/caddy/v2/caddyconfig/httpcaddyfile"
	"github.com/caddyserver/caddy/v2/modules/caddyhttp"
)

type HelloWorld struct {
	Text string `json:"text,omitempty"`
}

func (h HelloWorld) CaddyModule() caddy.ModuleInfo {
	return caddy.ModuleInfo{
		ID:  "http.handlers.hello_world",
		New: func() caddy.Module { return new(HelloWorld) },
	}
}

func (h *HelloWorld) Provision(ctx caddy.Context) error {
	h.Text = "Hello 世界"
	return nil
}

// Validate validates that the module has a usable config.
func (h HelloWorld) Validate() error {
	if h.Text == "" {
		return errors.New("the text is must!!!")
	}
	return nil
}

func (h *HelloWorld) ServeHTTP(w http.ResponseWriter, r *http.Request, next caddyhttp.Handler) error {
	err := next.ServeHTTP(w, r)
	if err != nil {
		return err
	}
	w.Write([]byte(h.Text))
	return nil
}

func init() {
	caddy.RegisterModule(&HelloWorld{})
	httpcaddyfile.RegisterHandlerDirective("hello_world", parseCaddyfile)
}

func parseCaddyfile(h httpcaddyfile.Helper) (caddyhttp.MiddlewareHandler, error) {
	hw := new(HelloWorld)
	return hw, nil
}

var (
	_ caddy.Provisioner           = (*HelloWorld)(nil)
	_ caddy.Validator             = (*HelloWorld)(nil)
	_ caddyhttp.MiddlewareHandler = (*HelloWorld)(nil)
)
