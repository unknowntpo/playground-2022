package main

import (
	"net/http"
	"os"

	"github.com/go-kit/kit/log"
	httptransport "github.com/go-kit/kit/transport/http"
)

func main() {
	var svc EchoService

	logger := log.NewLogfmtLogger(os.Stderr)

	svc = echoService{}
	svc = loggingMiddleware{logger, svc}

	echoHandler := httptransport.NewServer(
		makeEchoEndpoint(svc),
		decodeEchoRequest,
		encodeEchoResponse,
	)

	http.Handle("/echo", echoHandler)
	logger.Log("msg", "HTTP", "addr", ":4000")
	logger.Log(http.ListenAndServe(":4000", nil))
}
