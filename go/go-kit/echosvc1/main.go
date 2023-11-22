package main

import (
	"context"
	"encoding/json"
  "errors"
	"net/http"
  "log"

	"github.com/go-kit/kit/endpoint"
	httptransport "github.com/go-kit/kit/transport/http"
)

type EchoService interface {
  Echo(string)(string, error)
}

type echoService struct {}

func(echoService) Echo(s string)(string, error) {
  if s == "" {
    return "", ErrEmpty
  }
  return s, nil
}

var ErrEmpty = errors.New("empty string")

type echoRequest struct {
  S string `json:"s"`
}

type echoResponse struct {
  V string `json:"s"`
  Err string `json:"err,omitempty"`
}

func makeEchoEndpoint(svc echoService) endpoint.Endpoint {
  return func(_ context.Context, request any) (any, error) {
    req := request.(echoRequest)
    v, err := svc.Echo(req.S)
    if err != nil {
      return echoResponse{v, err.Error()}, nil
    }
    return echoResponse{V:v}, nil
  }
}

func decodeEchoRequest(_ context.Context, r *http.Request)(any, error) {
  var req echoRequest
  if err := json.NewDecoder(r.Body).Decode(&req); err!=nil {
    return nil, err
  }
  return req, nil
}

func encodeEchoResponse(_ context.Context, w http.ResponseWriter, resp any)error {
  return json.NewEncoder(w).Encode(resp)
}

func main() {
  svc := echoService{}
  echoHandler := httptransport.NewServer(
    makeEchoEndpoint(svc),
    decodeEchoRequest,
    encodeEchoResponse,
  )

  http.Handle("/echo", echoHandler)
  log.Fatal(http.ListenAndServe(":4000", nil))
}
