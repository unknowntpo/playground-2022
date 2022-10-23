package main

import (
	"fmt"
	"reflect"
)

func main() {
	i := 1
	ptrVal := reflect.ValueOf(&i)
	ptrVal.Elem().Set(reflect.ValueOf(2))
	fmt.Println(i)
}
