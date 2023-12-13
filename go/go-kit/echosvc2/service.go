package main

import (
	"context"
	"errors"

	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/trace"
)

type EchoService interface {
	Echo(ctx context.Context, word string) (string, error)
}

type echoService struct{}

func (echoService) Echo(ctx context.Context, word string) (string, error) {
	ctx, span := tracer.Start(ctx, "Echo", trace.WithAttributes(attribute.String("HelloFrom", "Echo method")))
	defer span.End()
	if word == "" {
		return "", ErrEmpty
	}
	return word, nil
}

var ErrEmpty = errors.New("empty string")
