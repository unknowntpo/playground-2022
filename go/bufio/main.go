// Click here and start typing.
package main

import (
	"bufio"
	"bytes"
	"fmt"
	"math/rand"
	"time"
)

func must(err error) {
	if err != nil {
		panic(err)
	}
}

// writing gets data from input and call buf.Write to write data to underlying writers.
// when there's no more data from input, we call buf.Flush to flush all data.
// what if we need to let buf been flushed in a period of time ?
func writing(buf *bufio.Writer, input <-chan []byte) (err error) {
	var data []byte
	var more = true
	t := time.NewTicker(100 * time.Millisecond)
	for err == nil {
		select {
		case data, more = <-input:
			if !more {
				goto cleanup
			}
			fmt.Printf("writing data %v to Writer\n", data)
			_, err = buf.Write(data)
			if err != nil {
				return err
			}
		case <-t.C:
			fmt.Println("flush is called by times up event")
			err = buf.Flush()
		}
	}

cleanup:
	fmt.Println("flush is called")
	err = buf.Flush()
	return
}

func dataGen() chan []byte {
	ch := make(chan []byte)
	go func() {
		for cnt := 1 << 20; cnt > 0; cnt-- {
			ch <- []byte(RandStringRunes(3))
			time.Sleep(50 * time.Millisecond)
		}
		close(ch)
	}()
	return ch
}

func init() {
	rand.New(rand.NewSource(time.Now().UnixNano()))
}

var letterRunes = []rune("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")

func RandStringRunes(n int) string {
	b := make([]rune, n)
	for i := range b {
		b[i] = letterRunes[rand.Intn(len(letterRunes))]
	}
	return string(b)
}

// a writer writes req data by sending data to writeCh
// and queue itself to waitQueue to wait for response
// reader waits for response
func main() {
	ch := dataGen()
	buf := bytes.NewBuffer([]byte{})
	writer := bufio.NewWriter(buf)
	must(writing(writer, ch))
	fmt.Println(buf.Len())
}
