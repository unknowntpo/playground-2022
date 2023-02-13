package main

import "testing"

func TestFibonacci(t *testing.T) {
	tests := []struct {
		input  string
		result int
	}{
		{"5", 5},
		{"10", 55},
	}

	for _, test := range tests {
		res := fibonacci(test.input)
		if res != test.result {
			t.Errorf("Expected fibonacci(%s) to be %d, got %d", test.input, test.result, res)
		}
	}
}