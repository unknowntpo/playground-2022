package main

import (
	"context"
	"net/http"
	"os"

	kitprometheus "github.com/go-kit/kit/metrics/prometheus"
	httptransport "github.com/go-kit/kit/transport/http"
	"github.com/go-kit/log"
	stdprometheus "github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

func main() {
	var svc EchoService

	logger := log.NewLogfmtLogger(os.Stderr)

	fieldKeys := []string{"method", "error"}
	requestCount := kitprometheus.NewCounterFrom(stdprometheus.CounterOpts{
		Namespace: "my_group",
		Subsystem: "echo_service",
		Name:      "request_count",
		Help:      "Number of request received.",
	}, fieldKeys)
	requestLatency := kitprometheus.NewSummaryFrom(stdprometheus.SummaryOpts{
		Namespace: "my_group",
		Subsystem: "echo_service",
		Name:      "request_latency_microseconds",
		Help:      "Total duration of requests in microseconds.",
	}, fieldKeys)

	svc = echoService{}
	svc = loggingMiddleware{logger, svc}
	svc = instrumentingMiddleware{requestCount, requestLatency, svc}

	echoHandler := httptransport.NewServer(
		makeEchoEndpoint(svc),
		decodeEchoRequest,
		encodeEchoResponse,
	)

	tp, err := initTracer()
	defer func() {
		tp.Shutdown(context.Background())
	}()
	must(err)
	tracer = tp.Tracer("echosvc2")

	http.Handle("/echo", echoHandler)
	http.Handle("/metrics", promhttp.Handler())

	logger.Log("msg", "HTTP", "addr", ":4000")
	logger.Log(http.ListenAndServe(":4000", nil))
}

// https://github.com/wavefrontHQ/opentelemetry-examples/tree/master/go-example
