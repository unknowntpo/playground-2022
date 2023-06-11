package main

import (
	"errors"
)

type Result[T, E any] struct {
	t     T
	e     E
	state state
}

type state int

const (
	RESULT_OK state = iota
	RESULT_ERR
)

func Err[T, E any](i E) Result[T, E] {
	return Result[T, E]{e: i, state: RESULT_ERR}
}

func Ok[T, E any](i T) Result[T, E] {
	return Result[T, E]{t: i, state: RESULT_OK}
}

func divide_by(x int, y int) Result[int, error] {
	if y == 0 {
		return Err[int, error](errors.New("divided by zero"))
	}
	return Ok[int, error](x)
}

// TODO: Match
// TODO: unwrap
func main() {
	r := divide_by(3, 0)
	Match(r, func() {

	})
}
