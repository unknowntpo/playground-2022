package main

import (
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
		if h.arr[pIdx] <= h.arr[curIdx] {
			h.arr[pIdx], h.arr[curIdx] = h.arr[curIdx], h.arr[pIdx]
			curIdx = pIdx
		} else {
			break
		}
	}
	return
}
func (h *heap) heapifyDown(idx int) {
	curIdx := idx
	for {
		leftChildIdx := leftChild(curIdx)
		rightChildIdx := rightChild(curIdx)

		wantChildIdx := h.getBiggerChildIdx(leftChildIdx, rightChildIdx)
		if h.arr[wantChildIdx] <= h.arr[curIdx] {
			h.arr[wantChildIdx], h.arr[curIdx] = h.arr[curIdx], h.arr[wantChildIdx]
			curIdx = wantChildIdx
		} else {
			break
		}
	}
	return
}

// get bigger
func (h *heap) getBiggerChildIdx(leftIdx, rightIdx int) int {
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

// has: bool
// ele: int
func (h *heap) Pop() (bool, int) {
	// move last element to first
	if len(h.arr) == 0 {
		return false, 0
	}
	out := h.arr[0]
	h.arr[0] = h.arr[len(h.arr)-1]
	h.heapifyDown(0)
	return true, out
}

func TestMaxHeap(t *testing.T) {
	testCases := []struct {
		nums []int
		want []int
	}{
		{[]int{1, 3, 2, 5, 1}, []int{5, 3, 2, 1}},
		{[]int{1}, []int{1}},
		{[]int{1, 2, 3, 4, 5}, []int{5, 4, 3, 2, 1}},
		{[]int{3, 1, 5, 1, 1}, []int{5, 3, 1}},
	}

	for _, tc := range testCases {
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
