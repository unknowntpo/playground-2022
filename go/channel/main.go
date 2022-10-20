package main

import (
	"fmt"
	"math/rand"
	"runtime/debug"
	"time"
)

func work() error {
	s1 := rand.NewSource(time.Now().UnixNano())
	r1 := rand.New(s1)

	if r1.Float32() > 0.5 {
		panic("panic")
	}
	return nil
}
func sub(c chan struct{}, errChan chan error) {
	// do some work
	defer func() {
		close(c)
		close(errChan)
	}()

	defer func() {
		if r := recover(); r != nil {
			errChan <- fmt.Errorf("panic in sub: %v, %v", r, string(debug.Stack()))
		}
	}()

	if err := work(); err != nil {
		errChan <- fmt.Errorf("failed to do work: %v", err)
	}
	c <- struct{}{}
}
func main() {
	c := make(chan struct{})
	errChan := make(chan error)
	go sub(c, errChan)

	select {
	case <-c:
		fmt.Println("PASS")
	case err := <-errChan:
		fmt.Println("FAIL", err)
	}
}
