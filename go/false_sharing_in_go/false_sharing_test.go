package main

import (
	"sync"
	"testing"
)

const CPU_COUNT = 4
const ADD_TIMES = 10000

func BenchmarkFalseSharing8(b *testing.B) {

	a := make([]int8, 4)

	for n := 0; n < b.N*ADD_TIMES; n++ {
		var wg sync.WaitGroup
		for i := 0; i < CPU_COUNT; i++ {
			wg.Add(1)
			go func(_i int) {
				for j := 0; j <= ADD_TIMES; j++ {
					incrementPos8(&a, _i)
				}
				wg.Done()
			}(i)
		}
		wg.Wait()
	}

}

func BenchmarkFalseSharing16(b *testing.B) {

	a := make([]int16, 4)

	for n := 0; n < b.N*ADD_TIMES; n++ {
		var wg sync.WaitGroup

		for i := 0; i < CPU_COUNT; i++ {
			wg.Add(1)
			go func(_i int) {
				for j := 0; j <= ADD_TIMES; j++ {
					incrementPos16(&a, _i)
				}
				wg.Done()
			}(i)
		}
		wg.Wait()
	}
}

func BenchmarkFalseSharing32(b *testing.B) {

	a := make([]int32, 4)

	for n := 0; n < b.N*ADD_TIMES; n++ {
		var wg sync.WaitGroup

		for i := 0; i < CPU_COUNT; i++ {
			wg.Add(1)
			go func(_i int) {
				for j := 0; j <= ADD_TIMES; j++ {
					incrementPos32(&a, _i)
				}
				wg.Done()
			}(i)
		}
		wg.Wait()
	}
}

func BenchmarkFalseSharing64(b *testing.B) {

	a := make([]int64, 4)

	for n := 0; n < b.N; n++ {
		var wg sync.WaitGroup

		for i := 0; i < CPU_COUNT; i++ {
			wg.Add(1)
			go func(_i int) {
				for j := 0; j <= ADD_TIMES; j++ {
					incrementPos64(&a, _i)
				}
				wg.Done()
			}(i)
		}
		wg.Wait()
	}
}
