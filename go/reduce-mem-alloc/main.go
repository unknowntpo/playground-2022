package main

import (
	"bytes"
	"crypto/sha256"
	"fmt"
	"math/rand"
	"os"
	"runtime/pprof"
	"strconv"
	"testing"
)

func foo(n int) string {
	var buf bytes.Buffer

	// Pre-compute x to avoid lots of memory allocation about string
	x := strconv.Itoa(n)
	for i := 0; i < 100000; i++ {
		buf.WriteString(x)
	}
	sum := sha256.Sum256(buf.Bytes())

	var b []byte
	for i := 0; i < int(sum[0]); i++ {
		x := sum[(i*7+1)%len(sum)] ^ sum[(i*5+3)%len(sum)]
		c := "abcdefghijklmnopqrstuvwxyz"[x%26]
		b = append(b, c)
	}
	return string(b)
}

func main() {
	cpufile, err := os.Create("cpu.pprof")
	if err != nil {
		panic(err)
	}
	err = pprof.StartCPUProfile(cpufile)
	if err != nil {
		panic(err)
	}
	defer cpufile.Close()
	defer pprof.StopCPUProfile()

	// ensure function output is accurate
	if foo(12345) == "aajmtxaattdzsxnukawxwhmfotnm" {
		fmt.Println("Test PASS")
	} else {
		fmt.Println("Test FAIL")
	}

	fmt.Println("Allocs:", int(testing.AllocsPerRun(100, func() {
		foo(rand.Int())
	})))
}
