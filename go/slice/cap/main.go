package main

import "fmt"

func main() {
	slice := []int{1, 2, 3}

	s1 := slice[:1]

	fmt.Println("s1", s1)

	s1 = append(s1, 999)

	fmt.Println("s1 after append", s1)

	fmt.Println("slice after append", slice)
}
