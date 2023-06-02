package main

import (
	"fmt"
	"reflect"
	"testing"
)

func TestMaxHeap(t *testing.T) {
	testCases := []struct {
		nums []int
		want []int
	}{
		{[]int{1, 3, 2, 5, 1}, []int{5, 3, 2, 1, 1}},
		{[]int{1}, []int{1}},
		{[]int{1, 2, 3, 4, 5}, []int{5, 4, 3, 2, 1}},
		{[]int{3, 1, 5, 1, 1}, []int{5, 3, 1}},
	}

	for _, tc := range testCases {
		t.Run(fmt.Sprintf("nums: %v", tc.nums), func(t *testing.T) {
			h := NewHeap()
			for _, n := range tc.nums {
				h.Push(n)
			}
			// pop
			out := make([]int, 0, len(tc.nums))
			for i := 0; i < len(tc.nums); i++ {
				has, got := h.Pop()
				if has {
					out = append(out, got)
				}
			}

			if !reflect.DeepEqual(out, tc.want) {
				t.Errorf("wrong result of max heap of %v, got %v, want %v", tc.nums, out, tc.want)
			}
		})

	}
}

// ----- real implementation of topKFrequent

func topKFrequent(nums []int, k int) []int {
	// Your implementation for the topKFrequent function goes here.
	h := NewHeap()
	for _, n := range nums {
		h.Push(n)
	}

	// pop
	out := make([]int, 0, k)
	for i := 0; i < k; i++ {
		_, got := h.Pop()
		out = append(out, got)
	}
	return out
}
