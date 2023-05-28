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
		{[]int{3, 1, 5, 1, 1}, []int{5, 3, 1, 1, 1}},
	}

	for _, tc := range testCases {
		t.Run(fmt.Sprintf("nums: %v", tc.nums), func(t *testing.T) {
			h := NewMaxHeap()
			for _, n := range tc.nums {
				h.Insert(n)
			}
			// pop
			out := make([]int, 0, len(tc.nums))
			for i := 0; i < len(tc.nums); i++ {
				got, has := h.Extract()
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
