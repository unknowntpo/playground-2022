package calculator

import (
	"encoding/json"
	"errors"
	"fmt"
	"log"
	"net/http"

	add "github.com/unknowntpo/playground-2022/go/caddy/multiverse/calculator/add"

	caddy "github.com/caddyserver/caddy/v2"
	"github.com/caddyserver/caddy/v2/caddyconfig/httpcaddyfile"
	"github.com/caddyserver/caddy/v2/modules/caddyhttp"
)

type Calculator struct {
	Text        string          `json:"text,omitempty"`
	OperandsRaw caddy.ModuleMap `json:"operands,omitempty" caddy:"namespace=calculator.operands"`
	Operands    []Operand
}

type Operand interface{}

func (c Calculator) CaddyModule() caddy.ModuleInfo {
	return caddy.ModuleInfo{
		ID:  "http.handlers.calculator",
		New: func() caddy.Module { return new(Calculator) },
	}
}

func (c *Calculator) Provision(ctx caddy.Context) error {
	c.Text = "Hello Calculator"
	// May use LoadModule to load module
	val, err := ctx.LoadModule(c, "OperandsRaw")
	if err != nil {
		return fmt.Errorf("loading certificate loader modules: %s", err)
	}
	// from tls.go
	// for modName, modIface := range val.(map[string]any) {
	// 	if modName == "automate" {
	// 		// special case; these will be loaded in later using our automation facilities,
	// 		// which we want to avoid doing during provisioning
	// 		if automateNames, ok := modIface.(*AutomateLoader); ok && automateNames != nil {
	// 			t.automateNames = []string(*automateNames)
	// 		} else {
	// 			return fmt.Errorf("loading certificates with 'automate' requires array of strings, got: %T", modIface)
	// 		}
	// 		continue
	// 	}
	// 	t.certificateLoaders = append(t.certificateLoaders, modIface.(CertificateLoader))
	// }
	for modName, modIface := range val.(map[string]any) {
		if modName == "add" {
			// special case; these will be loaded in later using our automation facilities,
			// which we want to avoid doing during provisioning
			if _, ok := modIface.(*add.Add); ok {
			} else {
				return fmt.Errorf("modIface is not %T, got %T", &add.Add{}, modIface)
			}
		}
		c.Operands = append(c.Operands, modIface.(add.Add))
	}

	return nil
}

// Validate validates that the module has a usable config.
func (c *Calculator) Validate() error {
	if c.Text == "" {
		return errors.New("the text is must!!!")
	}
	return nil
}

type CalculatorReq struct {
	Expr string `json:"expr"`
}

func (c *Calculator) ServeHTTP(w http.ResponseWriter, r *http.Request, next caddyhttp.Handler) error {
	err := next.ServeHTTP(w, r)
	if err != nil {
		return err
	}

	req := CalculatorReq{}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return nil
	}
	// TODO: caddy default logger ?
	log.Println("request", debug(req))
	dummyResp := "3"
	w.Write([]byte(dummyResp))
	return nil
}

func debug(i interface{}) string {
	b, err := json.Marshal(i)
	must(err)
	return string(b)
}

func must(err error) {
	if err != nil {
		panic(err)
	}
}
func init() {
	caddy.RegisterModule(&Calculator{})
	httpcaddyfile.RegisterHandlerDirective("calculator", parseCaddyfile)
}

func parseCaddyfile(h httpcaddyfile.Helper) (caddyhttp.MiddlewareHandler, error) {
	hw := new(Calculator)
	return hw, nil
}

var (
	_ caddy.Provisioner           = (*Calculator)(nil)
	_ caddy.Validator             = (*Calculator)(nil)
	_ caddyhttp.MiddlewareHandler = (*Calculator)(nil)
)
