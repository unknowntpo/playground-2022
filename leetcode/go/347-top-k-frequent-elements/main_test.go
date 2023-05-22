package main

import (
	"fmt"
	"reflect"
	"testing"
)

func NewHeap() *heap {
	h := &heap{
		arr: []int{},
	}
	return h
}

type heap struct {
	arr []int
}

func parent(childIdx int) int {
	return (childIdx - 1) / 2
}

func leftChild(idx int) int {
	return idx*2 + 1
}

func rightChild(idx int) int {
	return idx*2 + 2
}

func (h *heap) heapifyUp(idx int) {
	curIdx := idx
	for {
		pIdx := parent(curIdx)
		if h.arr[pIdx] < h.arr[curIdx] {
			h.swap(pIdx, curIdx)
			curIdx = pIdx
		} else {
			break
		}
	}
	fmt.Println("heapifyUp Done", h.arr)
	return
}

func (h *heap) swap(i1, i2 int) {
	h.arr[i1], h.arr[i2] = h.arr[i2], h.arr[i1]
}

func (h *heap) heapifyDown(idx int) {
	curIdx := idx
	for {
		fmt.Println("curIdx", curIdx)
		leftChildIdx := leftChild(curIdx)
		rightChildIdx := rightChild(curIdx)
		wantChildIdx := h.getBiggerChildIdx(leftChildIdx, rightChildIdx)
		fmt.Println("wantChildIdx", wantChildIdx)
		if h.arr[wantChildIdx] < h.arr[curIdx] {
			h.swap(wantChildIdx, curIdx)
			curIdx = wantChildIdx
		} else {
			break
		}
	}
	fmt.Println("heapifyDown done,", h.arr)
	return
}

// get bigger
func (h *heap) getBiggerChildIdx(leftIdx, rightIdx int) int {
	last := len(h.arr) - 1
	fmt.Println("left:", leftIdx, "right", rightIdx, "last", last)
	if leftIdx >= last {
		// prevent index out of range error
		return leftIdx
	}
	if h.arr[leftIdx] > h.arr[rightIdx] {
		return leftIdx
	} else {
		return rightIdx
	}
}

func (h *heap) Push(i int) {
	h.arr = append(h.arr, i)
	h.heapifyUp(len(h.arr) - 1)
	// heapifyUp for that element

}

/*
*
*

[1,3,2,5,1]


h: [1]

*/

// has: bool
// ele: int
func (h *heap) Pop() (bool, int) {
	// move last element to first
	if len(h.arr) == 0 {
		return false, 0
	}
	out := h.arr[0]
	last := len(h.arr) - 1
	h.arr[0] = h.arr[last]
	h.arr = h.arr[:last]
	h.heapifyDown(0)
	return true, out
}

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
