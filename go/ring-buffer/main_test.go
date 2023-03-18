package ring_test

import (
	"sync"
	"testing"

	. "github.com/onsi/ginkgo/v2"
	. "github.com/onsi/gomega"

	ring "github.com/unknowntpo/playground-2022/go/ring-buffer"
)

func TestBuffer(t *testing.T) {
	RegisterFailHandler(Fail)
	RunSpecs(t, "Ring")
}

var _ = Describe("Ring", func() {
	var (
		r *ring.Ring[int]
	)

	BeforeEach(func() {
		cap := 3
		r = ring.NewBuffer[int](cap)
	})

	When("Push element to ring", func() {
		Context("Push 1 element", func() {
			BeforeEach(func() {
				r.Push(1)
			})
			It("should got right result", func() {
				Expect(r.Len()).To(Equal(1))

				Expect(r.Pop()).To(Equal(1))
			})
		})
		Context("Push 2 elements", func() {
			BeforeEach(func() {
				r.Push(1)
				r.Push(2)
			})
			It("should got right result", func() {
				Expect(r.Len()).To(Equal(2))

				Expect(r.Pop()).To(Equal(1))
				Expect(r.Pop()).To(Equal(2))
			})
		})
		Context("Should handle overflow", func() {
			BeforeEach(func() {
				r.Push(1)
				r.Push(2)
				r.Push(3)
				r.Push(4)
				// h
				// t
				// 4 2 3
			})
			It("should got right result", func() {
				Expect(r.Len()).To(Equal(3))

				// Overflow, overwrite the old one
				Expect(r.Pop()).To(Equal(4))
				Expect(r.Pop()).To(Equal(2))
			})
		})
	})
})

func BenchmarkRingBuffer(b *testing.B) {
	b.Run("test push and pop", func(b *testing.B) {
		cap := 100000
		r := ring.NewBuffer[int](cap)
		pushFn := func(wg *sync.WaitGroup) {
			defer wg.Done()
			for i := 0; i < cap; i++ {
				r.Push(3)
			}
		}

		popFn := func(wg *sync.WaitGroup) {
			defer wg.Done()
			for i := 0; i < cap; i++ {
				r.Pop()
			}
		}

		for i := 0; i < b.N; i++ {
			var wg sync.WaitGroup
			wg.Add(2)

			go pushFn(&wg)
			go popFn(&wg)
			wg.Wait()
		}
	})
}
