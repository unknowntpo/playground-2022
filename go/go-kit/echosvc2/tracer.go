package main

import (
	"context"
	"fmt"

	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/exporters/jaeger"
	"go.opentelemetry.io/otel/exporters/otlp/otlptrace"
	"go.opentelemetry.io/otel/exporters/otlp/otlptrace/otlptracegrpc"
	"go.opentelemetry.io/otel/exporters/stdout/stdouttrace"
	"go.opentelemetry.io/otel/sdk/resource"
	sdktrace "go.opentelemetry.io/otel/sdk/trace"
	semconv "go.opentelemetry.io/otel/semconv/v1.21.0"
	"go.opentelemetry.io/otel/trace"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

var tracer trace.Tracer

func newStdoutExporter() (*stdouttrace.Exporter, error) {
	exporter, err := stdouttrace.New(stdouttrace.WithPrettyPrint())
	if err != nil {
		return nil, fmt.Errorf("creating stdout exporter: %w", err)
	}
	return exporter, nil
}

func newOLTPExporter(ctx context.Context) (*otlptrace.Exporter, error) {
	conn, err := grpc.DialContext(ctx, "http://localhost:4317", grpc.WithTransportCredentials(insecure.NewCredentials()), grpc.WithBlock())
	must(err)
	traceExporter, err := otlptracegrpc.New(ctx, otlptracegrpc.WithGRPCConn(conn))
	return traceExporter, err
}

func newJaegerExporter(url string) (*jaeger.Exporter, error) {
	// Create the Jaeger exporter
	exp, err := jaeger.New(jaeger.WithCollectorEndpoint(jaeger.WithEndpoint(url)))
	return exp, err
}

func must(err error) {
	if err != nil {
		panic(err)
	}
}

func newResource(ctx context.Context) (*resource.Resource, error) {
	res, err := resource.New(ctx,
		resource.WithAttributes(
			semconv.ServiceNameKey.String("echosvc2"),
			attribute.String("application", "echosvc2"),
		),
	)
	return res, err
}

func newTraceProvider(res *resource.Resource, bsp sdktrace.SpanProcessor) *sdktrace.TracerProvider {
	tracerProvider := sdktrace.NewTracerProvider(
		sdktrace.WithSampler(sdktrace.AlwaysSample()),
		sdktrace.WithResource(res),
		sdktrace.WithSpanProcessor(bsp),
	)
	return tracerProvider
}

func initTracer() (*sdktrace.TracerProvider, error) {
	ctx := context.Background()
	res, err := newResource(ctx)
	if err != nil {
		return nil, err
	}
	// Ref: https://github.com/open-telemetry/opentelemetry-go/blob/exporters/jaeger/v1.17.0/example/jaeger/main.go#L70
	traceExporter, err := newJaegerExporter("http://localhost:14268/api/traces")
	if err != nil {
		return nil, err
	}
	// traceExporter, err := newStdoutExporter()
	// if err != nil {
	// 	return nil, err
	// }
	// traceExporter, err := newOLTPExporter(ctx, conn)
	// if err != nil {
	// 	return nil, err
	// }
	batchSpanProcessor := sdktrace.NewBatchSpanProcessor(traceExporter)
	tracerProvider := newTraceProvider(res, batchSpanProcessor)
	otel.SetTracerProvider(tracerProvider)
	return tracerProvider, nil
}
