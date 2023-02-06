//go:build wireinject
// +build wireinject

// NOTE: We should exclude this file
// Ref: https://bingdoal.github.io/backend/2022/05/golang-dependency-injection-google-wire/
package main

import "github.com/google/wire"

func InitializeEvent() Event {
	wire.Build(NewEvent, NewGreeter, NewMessage)
	return Event{}
}
