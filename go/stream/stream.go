package stream

/*

map: Stream[T] -> fn([T]) T -> Stream[T]
map: func[T, U](input Stream[T], mapFn func(T) U)) Stream[U]

reduce: Stream[T] -> reduceFn[T] -> T

forEach: Stream[T] -> fn[T]

// return error ?

*/

// T: target, U: accumulator
type Stream[T, U any] interface {
	Next() (T, bool)
	Produce(T)
	Map(func(T) U) Stream[T, U]
	Reduce(reduceFn func(target T, accumulator U) U, initValue U) U
	FromSlice(slice []T) Stream[T, U]
	Close()
}

type stream[T, U any] struct {
	ele T
	ch  chan T
}

func (s *stream[T, U]) Next() (T, bool) {
	return s.ele, false
}

func (s *stream[T, U]) Produce(ele T) {
	s.ch <- ele
}

func (s *stream[T, U]) FromSlice(slice []T) Stream[T, U] {
	go func() {
		for _, e := range slice {
			s.Produce(e)
		}
		close(s.ch)
	}()
	return s
}

func (s *stream[T, U]) Close() {
	close(s.ch)
}

func (s *stream[T, U]) Map(mapFn func(T) U) Stream[T, U] {
	next := NewStream[T, U]()
	go func() {
		for e := range s.ch {
			next.Produce(mapFn(e))
		}
		next.Close()
	}()
	return next
}

func (s *stream[T, U]) Reduce(reduceFn func(target T, accumulator U) U, initValue U) U {
	acc := initValue
	for e := range s.ch {
		acc = reduceFn(e, acc)
	}
	return acc
}

func NewStream[T, U any]() Stream[T, U] {
	stream := &stream[T, U]{ch: make(chan T, 10)}
	return stream
}

/*
// file stream
Stream[[]byte]

*/
