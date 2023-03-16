package main

import (
	"sync"
	"testing"
)

func BenchmarkAdd(b *testing.B) {
	Limit := 10000
	workerNum := 8
	for i := 0; i < b.N; i++ {
		counter := Counter{}

		var wg sync.WaitGroup
		wg.Add(workerNum)

		for workerIdx := 0; workerIdx < workerNum; workerIdx++ {
			go func() {
				defer wg.Done()
				for i := 0; i < Limit; i++ {
					counter.Add(1)
				}
			}()
		}
	}
}
