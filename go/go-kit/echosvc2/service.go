package main

import "errors"

type EchoService interface {
	Echo(string) (string, error)
}

type echoService struct{}

func (echoService) Echo(s string) (string, error) {
	if s == "" {
		return "", ErrEmpty
	}
	return s, nil
}

var ErrEmpty = errors.New("empty string")
