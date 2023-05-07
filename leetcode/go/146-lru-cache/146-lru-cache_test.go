package main

import (
	"container/list"
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
	l := list.New()
	_ = l
}

type Node[T any] struct {
	Val  T
	list *List[T]
	prev *Node[T]
	next *Node[T]
}

func (n *Node[T]) String() string {
	return fmt.Sprint(n.Val)
}

func (n *Node[T]) Next() *Node {
	if p := n.next; n.next != nil && p != &n.l.root {
		return p
	}
	return nil
}

func (n *Node[T]) Prev() *Node {
	return nil
}

type List[T any] struct {
	root Node[T]
	len  int
}

func (l *List[T]) NewNode(val T) *Node[T] {
	return &Node[T]{Val: val, list: l}
}

func NewList[T any]() *List[T] {
	r := &Node[T]{}
	r.next = r
	r.prev = r
	return &List[T]{root: r}
}

func (l *List[T]) IsEmpty() bool {
	return l.len == 0
}

func (l *List[T]) PushBack(val T) error {
	n := &Node[T]{Val: val}
	if l.IsEmpty() {
		l.root.next = n
		l.root.prev = n
		l.len++
		return nil
	}

	n.next = &l.root
	n.prev = &l.root.prev
	l.root.prev = n
	l.len++

	return nil
}

func (l *List[T]) String() string {
	out := make([]string, 0, l.len)

	for cur := l.root; cur != cur.next; cur = cur.next {
		out = append(out, cur.String())
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
