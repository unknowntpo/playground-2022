package main

import (
	"context"
	"encoding/json"
	"net/http"

	"github.com/go-kit/kit/endpoint"
)

type echoRequest struct {
	S string `json:"s"`
}

type echoResponse struct {
	V   string `json:"s"`
	Err string `json:"err,omitempty"`
}

func makeEchoEndpoint(svc EchoService) endpoint.Endpoint {
	return func(_ context.Context, request any) (any, error) {
		req := request.(echoRequest)
		v, err := svc.Echo(req.S)
		if err != nil {
			return echoResponse{v, err.Error()}, nil
		}
		return echoResponse{V: v}, nil
	}
}

func decodeEchoRequest(_ context.Context, r *http.Request) (any, error) {
	var req echoRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		return nil, err
	}
	return req, nil
}

func encodeEchoResponse(_ context.Context, w http.ResponseWriter, resp any) error {
	return json.NewEncoder(w).Encode(resp)
}
