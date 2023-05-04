package main

import (
	"fmt"
	"github.com/stretchr/testify/assert"
	"strings"
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

func TestList(t *testing.T) {
	t.Run("PushBack", func(t *testing.T) {
		l := NewList[int]()
		l.PushBack(1)
		l.PushBack(2)
		l.PushBack(3)

		assert.Equal(t, 1, l.head.Val)
		assert.Equal(t, 3, l.tail.Val)
		assert.Equal(t, "[1,2,3]", l.String())
	})
}

type Node[T any] struct {
	Val  T
	prev *Node[T]
	next *Node[T]
}

func (n *Node[T]) String() string {
	return fmt.Sprint(n.Val)
}

type List[T any] struct {
	head *Node[T]
	tail *Node[T]
	len  int
}

func NewList[T any]() *List[T] {
	return &List[T]{}
}

func (l *List[T]) IsEmpty() bool {
	return l.len == 0
}

func (l *List[T]) PushBack(val T) error {
	if l.IsEmpty() {
		n := &Node[T]{Val: val}
		n.next = nil
		n.prev = nil
		l.head = n
		l.tail = n
		l.len += 1
		return nil
	}
	n := &Node[T]{Val: val}
	n.prev = l.tail
	n.next = nil
	// connect to tail of list
	l.tail.next = n
	l.tail = n
	l.len += 1
	return nil
}

func (l *List[T]) String() string {
	out := make([]string, 0, l.len)

	for lp := &l.head; *lp != nil; lp = &(*lp).next {
		out = append(out, (*lp).String())
	}
	return fmt.Sprintf("[%s]", strings.Join(out, ","))
}

func (l *List[T]) PushHead() error {
	return nil
}

func (l *List[T]) PopHead() error {
	return nil
}

func (l *List[T]) Delete(val T) error {
	return nil
}
