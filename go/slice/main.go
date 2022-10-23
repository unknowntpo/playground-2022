package main

import "fmt"

func main() {
	s := []int{1, 2, 3}
	sPtr := &s

	reset := func(sPtr *[]int) {
		s := *sPtr

		for i := range s {
			s[i] = 0
		}
	}
	reset(sPtr)
	fmt.Println(s)
}
