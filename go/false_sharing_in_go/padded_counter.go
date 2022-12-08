package main

import "sync/atomic"

type PaddedCounter struct {
	v1 uint64
	p1 [8]uint64
	v2 uint64
	p2 [8]uint64
	v3 uint64
	p3 [8]uint64
}

func (pc *PaddedCounter) Increment() {
	atomic.AddUint64(&pc.v1, 1)
	atomic.AddUint64(&pc.v2, 1)
	atomic.AddUint64(&pc.v3, 1)
}
