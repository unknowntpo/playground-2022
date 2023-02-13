package main

import (
	"runtime"
	"sync"
	"testing"
)

func BenchmarkFalseSharing(b *testing.B) {
	runtime.GOMAXPROCS(runtime.NumCPU())

	var wg sync.WaitGroup
	wg.Add(2)

	// two variables that are likely to map to the same cache line
	var x, y int64

	go func() {
		defer wg.Done()
		for i := 0; i < b.N; i++ {
			x++
		}
	}()

	go func() {
		defer wg.Done()
		for i := 0; i < b.N; i++ {
			y++
		}
	}()

	wg.Wait()
}

func BenchmarkNoFalseSharing(b *testing.B) {
	runtime.GOMAXPROCS(runtime.NumCPU())

	var wg sync.WaitGroup
	wg.Add(2)

	// two variables that are padded to ensure they map to separate cache lines
	var x [8]int64
	var y [8]int64

	go func() {
		defer wg.Done()
		for i := 0; i < b.N; i++ {
			x[0]++
		}
	}()

	go func() {
		defer wg.Done()
		for i := 0; i < b.N; i++ {
			y[0]++
		}
	}()

	wg.Wait()
}
