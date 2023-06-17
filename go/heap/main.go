package main

import (
	"fmt"
)

type MaxHeap struct {
	array []int
}

func NewMaxHeap() *MaxHeap {
	m := &MaxHeap{}
	m.array = []int{}
	return m
}

func (h *MaxHeap) Insert(key int) {
	h.array = append(h.array, key)
	h.maxHeapifyUp(len(h.array) - 1)
}

func (h *MaxHeap) maxHeapifyUp(index int) {
	for h.array[parent(index)] < h.array[index] {
		h.swap(parent(index), index)
		index = parent(index)
	}
}

/*
*
*[2, 3, 2, 1]
*[3, 2, 2, 1]
*
* l: 3
* r: 4
* can: 4
*[1, 3, 2, 5, 1]
* [3 2 1]
* [1,3,2,1]
* cur: 1
* l: 1 *2+1= 3 o
* r: 1 *2+2= 4 x
* can = l = 3
* [3,1,2,1]
* cur: 1
 */

func (h *MaxHeap) maxHeapifyDown(index int) {
	curIdx := index
	for h.isValidIdx(curIdx) {
		fmt.Println("curIdx", curIdx, "arr", h.array)
		l := left(curIdx)
		r := right(curIdx)
		var candidate int
		if !h.isValidIdx(l) {
			// l out of bound, ie: r is also out of bound
			return
		} else if !h.isValidIdx(r) {
			// l is valid, but r out of bound
			candidate = l
		} else {
			// both of them are valid, get max value
			candidate = h.getMaxValueOfIdx(l, r)
		}
		if h.array[curIdx] < h.array[candidate] {
			h.array[curIdx], h.array[candidate] = h.array[candidate], h.array[curIdx]
			curIdx = candidate
		} else {
			// not need to compare
			return
		}
	}
}

// given index, find out max value by given index
func (h *MaxHeap) getMaxValueOfIdx(l, r int) int {
	if h.array[l] >= h.array[r] {
		return l
	} else {
		return r
	}
}

func (h *MaxHeap) isValidIdx(idx int) bool {
	return 0 <= idx && idx < len(h.array)
}

func (h *MaxHeap) Extract() (int, bool) {
	if len(h.array) == 0 {
		fmt.Println("error: can not extract because array length is 0")
		return 0, false
	}
	extracted := h.array[0]
	l := len(h.array) - 1
	h.array[0] = h.array[l]
	h.array = h.array[:l]
	h.maxHeapifyDown(0)
	return extracted, true
}

func parent(i int) int {
	return (i - 1) / 2
}

func left(i int) int {
	return 2*i + 1
}

func right(i int) int {
	return 2*i + 2
}

func (h *MaxHeap) swap(i1, i2 int) {
	h.array[i1], h.array[i2] = h.array[i2], h.array[i1]
}
