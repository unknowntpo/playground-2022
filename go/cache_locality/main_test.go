package main

import (
	"sync"
	"testing"
	"time"
	// "x/sys/cpu"
)

// in macos, 128
// hw.cachelinesize: 128
type NoPad struct {
	Cnt0 int64 // make cache miss
	Cnt1 int64
}

type Pad struct {
	Cnt0 int64 // make cache miss
	_    int64 // padding
	Cnt1 int64
}

func BenchmarkFalseSharing(b *testing.B) {
	Target := 1 << 10
	b.Run("NoPad", func(b *testing.B) {
		for i := 0; i < b.N; i++ {
			s := &NoPad{}

			var wg sync.WaitGroup
			wg.Add(2)
			go func() {
				for j := 0; j < Target; j++ {
					s.Cnt0++
					// sleep()
				}
				wg.Done()
			}()
			go func() {
				for j := 0; j < Target; j++ {
					s.Cnt1++
					// sleep()
				}
				wg.Done()
			}()
			wg.Wait()
		}
	})
	b.Run("Pad", func(b *testing.B) {
		for i := 0; i < b.N; i++ {
			s := &Pad{}

			var wg sync.WaitGroup
			wg.Add(2)
			go func() {
				for j := 0; j < Target; j++ {
					s.Cnt0++
					// sleep()
				}
				wg.Done()
			}()
			go func() {
				for j := 0; j < Target; j++ {
					s.Cnt1++
					// sleep()
				}
				wg.Done()
			}()
			wg.Wait()
		}
	})
}

func sleep() {
	time.Sleep(3 * time.Microsecond)
}
