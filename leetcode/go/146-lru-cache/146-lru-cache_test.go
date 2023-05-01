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
