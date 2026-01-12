package main

import (
	"fmt"
	"iter"
)

func Range(n int) iter.Seq[int] {
	return func(yield func(int) bool) {
		for i := 0; i < n; i++ {
			if !yield(i) {
				return
			}
		}
	}
}

func Enumerate[T any](s []T) iter.Seq2[int, T] {
	return func(yield func(int, T) bool) {
		for i, v := range s {
			if !yield(i, v) {
				return
			}
		}
	}
}

func Evens() iter.Seq[int] {
	return func(yield func(int) bool) {
		for i := 0; ; i += 2 {
			if !yield(i) {
				return
			}
		}
	}
}

func main() {
	fmt.Println("Range(5):")
	for v := range Range(5) {
		fmt.Print(v, " ")
	}
	fmt.Println()

	fmt.Println("\nEnumerate:")
	for i, s := range Enumerate([]string{"a", "b", "c"}) {
		fmt.Printf("%d:%s", i, s)
	}
	fmt.Println()

	fmt.Println("\nFirst 5 evens:")
	count := 0
	for v := range Evens() {
		fmt.Print(v, " ")
		count++
		if count >= 5 {
			break
		}
	}
	fmt.Println()
}
