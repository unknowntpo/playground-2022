package main

import (
	"fmt"
	"time"
)

func main() {
	s := []int{}
	for i := 0; i < 100; i++ {
		time.Sleep(500 * time.Millisecond)
		s = append(s, i)
		fmt.Println(s)
	}
}
