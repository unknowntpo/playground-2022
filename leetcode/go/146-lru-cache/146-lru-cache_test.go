package main

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

type LRUCache struct {
	cap int
}

func (c *LRUCache) Get(key int) int {
	return key
}

func (c *LRUCache) Put(key int, value int) {
	return
}

func NewLRUCache(capacity int) *LRUCache {
	return &LRUCache{cap: capacity}
}

func TestLRUCache(t *testing.T) {
	t.Run("Normal", func(t *testing.T) {
		c := NewLRUCache(2)
		c.Put(1, 1)                   // cache is {1=1}
		c.Put(2, 2)                   // cache is {1=1, 2=2}
		assert.Equal(t, 1, c.Get(1))  // return 1
		c.Put(3, 3)                   // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
		assert.Equal(t, -1, c.Get(2)) // returns -1 (not found)
		c.Put(4, 4)                   // LRU key was 1, evicts key 1, cache is {4=4, 3=3}
		assert.Equal(t, -1, c.Get(1)) // return -1 (not found)
		assert.Equal(t, 3, c.Get(3))  // return 3
		assert.Equal(t, 4, c.Get(3))  // return 4
	})
}

type Node[T any] struct {
	Val  T
	prev *Node[T]
	next *Node[T]
}

type List[T any] struct {
	head *Node[T]
	tail *Node[T]
}

func NewList[T any]() *List[T] {
	return &List[T]{}
}

func (l *List[T]) IsEmpty() bool {
	return l.head == nil
}

func (l *List[T]) PushFront(val T) error {
	if l.IsEmpty() {
		l.head = &Node[T]{Val: val}
		l.tail = l.head
		l.head.next = l.head
	}
	return nil
}

func (l *List[T]) PushBack() error {
	return nil
}

func (l *List[T]) PopHead() error {
	return nil
}

func (l *List[T]) Delete(val T) error {
	return nil
}
