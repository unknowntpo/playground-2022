package main

import (
	"fmt"
	"runtime"
	"sync"
)

func main() {
	numCPUs := runtime.NumCPU()
	fmt.Println("Number of CPUs:", numCPUs)

	// Pin the main goroutine to a specific CPU
	runtime.LockOSThread()
	defer runtime.UnlockOSThread()

	fmt.Printf("Main Goroutine running on CPU %d\n", runtime.GOMAXPROCS(0))

	var wg sync.WaitGroup
	wg.Add(numCPUs)

	for i := 0; i < numCPUs; i++ {
		go func(id int) {
			defer wg.Done()

			// Set the maximum number of CPUs that can execute Go code simultaneously
			runtime.GOMAXPROCS(id + 1)

			// Pin the goroutine to a specific CPU
			runtime.LockOSThread()
			defer runtime.UnlockOSThread()

			fmt.Printf("Goroutine %d running on CPU %d\n", id, runtime.GOMAXPROCS(0))
		}(i)
	}

	wg.Wait()
}
