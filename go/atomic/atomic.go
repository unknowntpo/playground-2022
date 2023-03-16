package main

import (
	"fmt"
	"sync/atomic"
)

type Counter struct{ cnt int64 }

func (c *Counter) Add(num int64) {
	atomic.AddInt64(&c.cnt, num)
}

func (c *Counter) Get() int64 {
	return atomic.LoadInt64(&c.cnt)
}

func main() {
	var c Counter
	c.Add(1)
	c.Add(2)

	fmt.Println(c.Get())

}
