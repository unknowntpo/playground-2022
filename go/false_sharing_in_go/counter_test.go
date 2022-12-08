package main

import (
	"sync"
	"testing"
)

const NCPU = 4
const INCREMENT_TIME = 10000

func testIncrementAll(c Counter) {

	wg := sync.WaitGroup{}
	wg.Add(NCPU)
	for cpu := 0; cpu < NCPU; cpu++ {
		go func(i int) {
			for j := 0; j < INCREMENT_TIME; j++ {
				c.Increment()
			}
			wg.Done()
		}(cpu)
	}
	wg.Wait()
}

func BenchmarkNoPad(b *testing.B) {
	counter := &NotPaddedCounter{}
	b.ResetTimer()
	testIncrementAll(counter)
}

func BenchmarkPad(b *testing.B) {
	counter := &PaddedCounter{}
	b.ResetTimer()
	testIncrementAll(counter)
}
