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
	Reduce(reduceFn func(accumulator U, target T) U, initValue U) U
}

type stream[T, U any] struct {
	ele T
	ch  chan T
}

func (s *stream[T, U]) Next() (T, bool) {
	return s.ele, false
}

func (s *stream[T, U]) Produce(ele T) {
}

func (s *stream[T, U]) Map(func(T) U) Stream[T, U] {
	return nil
}
func (s *stream[T, U]) Reduce(reduceFn func(accumulator U, target T) U, initValue U) U {
	return initValue
}

/*
// file stream
Stream[[]byte]

*/
