package main

import "sync/atomic"

type NotPaddedCounter struct {
	v1 uint64
	v2 uint64
	v3 uint64
}

func (pc *NotPaddedCounter) Increment() {
	atomic.AddUint64(&pc.v1, 1)
	atomic.AddUint64(&pc.v2, 1)
	atomic.AddUint64(&pc.v3, 1)
}
