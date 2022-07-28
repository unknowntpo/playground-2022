package main

import (
	"fmt"
)

func main() {
	datas := []int{1, 2, 3, 4, 5, 6, 7, 8}
	pqProtocolLimit := 3
	var step int
	for cur := 0; cur < len(datas); cur += step {
		if len(datas)-cur < pqProtocolLimit {
			step = len(datas) - cur
		} else {
			step = pqProtocolLimit
		}
		bulk := datas[cur : cur+step]
		fmt.Println("bulk ", bulk)
	}
}
