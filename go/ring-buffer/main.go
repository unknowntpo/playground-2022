package ring

import "sync"

type Ring[T any] struct {
	mu   sync.Mutex
	buf  []T
	head int64
	tail int64
	size int64
}

func NewBuffer[T any](size int) *Ring[T] {
	return &Ring[T]{buf: make([]T, size)}
}

// Init state
// head = 0, tail = 0, size = 0
// Full state
// head = any, tail = any, size = len(buf)
func (r *Ring[T]) Push(item T) {
	// TODO: Use Lock
	r.mu.Lock()
	defer r.mu.Unlock()

	r.buf[r.head] = item

	isLastIdx := int(r.head) == len(r.buf)-1
	if isLastIdx {
		r.head = 0
	} else {
		r.head++
		r.size += 1
	}
}

func (r *Ring[T]) Pop() T {
	// TODO: Use Lock
	r.mu.Lock()
	defer r.mu.Unlock()

	oldTail := r.tail
	lastIdx := len(r.buf) - 1
	if int(r.tail) < lastIdx {
		r.tail++
	} else {
		r.tail = 0
	}
	r.size -= 1
	return r.buf[oldTail]
}

func (r *Ring[T]) Cap() int {
	// Because we are implementing fix sized ring buffer
	return len(r.buf)
}

func (r *Ring[T]) Len() int {
	return int(r.size)
}
