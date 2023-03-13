package main

import (
	"fmt"
	"runtime"
	"time"
)

func main() {
	// Set the maximum number of CPUs that can be executing simultaneously to 1.
	runtime.GOMAXPROCS(1)

	// Pin the current goroutine to processor 0.
	runtime.LockOSThread()

	Sin()
	// Wait for the goroutines to finish executing.
	fmt.Scanln()
}

func Sin() {
	counter := int64(0)
	for {
		if counter > 1<<62 {
			time.Sleep(30000 * time.Millisecond)
			counter = 0
			continue
		}
		counter += 1
	}
}
