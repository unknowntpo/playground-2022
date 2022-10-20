package main

import (
	"fmt"
	"net/http"
	"strconv"
)

// computeE computes the approximation of e by running a fixed number of iterations.
func computeE(iterations int64) float64 {
	res := 2.0
	fact := 1.0

	for i := int64(2); i < iterations; i++ {
		fact *= float64(i)
		res += 1 / fact
	}
	return res
}

func main() {
	http.HandleFunc("/e", func(w http.ResponseWriter, r *http.Request) {
		// Parse iters argument from get request, use default if not available.
		// ... removed for brevity ...
		itersStr := r.URL.Query().Get("iters")
		iters, err := strconv.Atoi(itersStr)
		must(err)
		w.Write([]byte(fmt.Sprintf("e = %0.4f\n", computeE(int64(iters)))))
	})
	// Start server...
	http.ListenAndServe(":9090", nil)
}

func must(err error) {
	if err != nil {
		panic(err)
	}
}
