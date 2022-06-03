package main

import "fmt"

func inner() {
	panic("something goes wrong!")
}

func middle() {
	defer func() {
		if r := recover(); r != nil {
			fmt.Println("recover panic in middle!", r)
		}
	}()

	inner()
}

func outer() {
	defer func() {
		if r := recover(); r != nil {
			fmt.Println("recover panic in outer!", r)
		}
	}()
	middle()
}

func main() {
	outer()
}
