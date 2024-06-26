package stream

import "fmt"

// T: target, U: accumulator
type Stream[T any] interface {
	Next() (T, bool)
	Produce(T)
	Reduce(reduceFn func(target T, accumulator T) T, initValue T) T
	FromSlice(slice []T) Stream[T]
	Close()
}

type stream[T any] struct {
	ch chan T
}

func (s *stream[T]) Next() (T, bool) {
	e, more := <-s.ch
	return e, more
}

func (s *stream[T]) Produce(e T) {
	s.ch <- e
}

func (s *stream[T]) FromSlice(slice []T) Stream[T] {
	go func() {
		for _, e := range slice {
			fmt.Println("FromSlice: produce e ", e)
			s.Produce(e)
		}
		fmt.Println("FromSlice: closing s")
		s.Close()
		fmt.Println("FromSlice: closed s")
	}()
	return s
}

func (s *stream[T]) Close() {
	close(s.ch)
}

func Map[T, U any](s Stream[T], mapFn func(T) U) Stream[U] {
	next := NewStream[U]()
	go func() {
		for e, more := s.Next(); more; e, more = s.Next() {
			fmt.Println("Map: got e ", e)
			next.Produce(mapFn(e))
		}
		next.Close()
	}()
	return next
}

func (s *stream[T]) Reduce(reduceFn func(target T, accumulator T) T, initValue T) T {
	acc := initValue
	for e, more := s.Next(); more; e, more = s.Next() {
		fmt.Println("Reduce got e, ", e)
		acc = reduceFn(e, acc)
	}
	return acc
}

func NewStream[T any]() Stream[T] {
	stream := &stream[T]{ch: make(chan T, 10)}
	return stream
}

/*
// file stream
Stream[[]byte]

*/
