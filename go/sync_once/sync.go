// You can edit this code!
// Click here and start typing.
package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

type Question struct {
	ans  string
	once sync.Once
}

func (q *Question) Answer(ans string) {
	q.once.Do(func() {
		q.ans = ans
		done <- true
	})
}

var done = make(chan bool)

func main() {
	q := &Question{}
	for i := 0; i < 10; i++ {
		go func(i int) {
			time.Sleep(time.Duration(rand.Intn(100)) * time.Millisecond)
			q.Answer(fmt.Sprintf("by %d", i))
		}(i)
	}
	for {
		select {
		case <-done:
			fmt.Println("question ans by: ", q.ans)
      return
		default:
		}
	}
}

