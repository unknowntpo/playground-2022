package main

import (
	"fmt"

	"gonum.org/v1/gonum/interp"
)

func must(err error) {
	if err != nil {
		panic(err)
	}
}

func main() {
	fmt.Println("vim-go")
	pl := &interp.PiecewiseLinear{}
	xs := []float64{1.0, 3.1, 3.3, 4.1, 5.1}
	ys := []float64{100, 201, 331, 431, 565}
	must(pl.Fit(xs, ys))
	x := 3.4
	fmt.Printf("x: %9f, y: %9f\n", x, pl.Predict(x))
}
