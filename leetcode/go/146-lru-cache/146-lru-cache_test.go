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

		length := 3
		for i := 0; i < length; i++ {
			l.PushBack(i)
		}

		assert.Equal(t, length, l.Len(), "length should be expected")

		i := 0
		for e := l.Front(); e != nil; e = e.Next() {
			assert.Equal(t, i, e.Val)
			i++
		}
		assert.Equal(t, "[0,1,2]", l.String(), "l.String() should return correct result")
	})
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

/*
[&0 r &0]

[&r 0 &0]

[&0 1 &r]
*/

func (n *Node[T]) Next() *Node[T] {
	if p := n.next; n.next != nil && p != &n.list.root {
		return p
	}
	return nil
}

func (n *Node[T]) Prev() *Node[T] {
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
	l := &List[T]{}
	l.root.next = &l.root
	l.root.prev = &l.root
	return l
}

func (l *List[T]) IsEmpty() bool {
	return l.len == 0
}

/*
[&1 r &0]

[&r 0 &0]

[&0 1 &r]

--

[&1 r &0]

[&r 0 &1]

[&0 1 &r]

---

[&r r &0]

[&r 0 x]
*/
func (l *List[T]) PushBack(val T) error {
	n := l.NewNode(val)
	if l.IsEmpty() {
		l.root.next = n
	}
	n.prev = l.root.prev
	l.root.prev.next = n
	l.root.prev = n
	n.next = &l.root
	l.len++
	return nil
}

func (l *List[T]) Front() *Node[T] {
	if l.len == 0 {
		return nil
	}
	return l.root.next
}

func (l *List[T]) Len() int {
	return l.len
}

func (l *List[T]) String() string {
	out := make([]string, 0, l.len)

	for e := l.Front(); e != nil; e = e.Next() {
		out = append(out, e.String())
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
