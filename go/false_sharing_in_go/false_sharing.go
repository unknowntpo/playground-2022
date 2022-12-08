package main

func incrementPos8(a *[]int8, pos int) {
	(*a)[pos]++
}

func incrementPos16(a *[]int16, pos int) {
	(*a)[pos]++
}

func incrementPos32(a *[]int32, pos int) {
	(*a)[pos]++
}

func incrementPos64(a *[]int64, pos int) {
	(*a)[pos]++
}
