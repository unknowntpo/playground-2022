package main

import (
	"fmt"
	"strings"
)

func main() {
	strs := []string{"a", "b", "c", "d"}
	fmt.Println(Map(strs, strings.ToUpper))
}

func Map[T any](datas []T, f func(data T) T) []T {
	for i := range datas {
		datas[i] = f(datas[i])
	}
	return datas
}
