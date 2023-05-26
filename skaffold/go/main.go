package main

import (
	"fmt"
	"time"
)

func main() {
	for {
		fmt.Println("Hello Eric")
		time.Sleep(1 * time.Second)
	}
}
