package main

import (
	"fmt"
	"time"
)

func main() {
	for i := 0; i < 100; i++ {
		time.Sleep(500 * time.Millisecond)
		fmt.Println(add(1, 2))
	}
}

//go:noinline
func add(a, b int) int {
	return a + b
}
