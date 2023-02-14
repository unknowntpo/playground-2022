package main

import (
	"sync"
)

type UnifyContainer []UnifyContainerRow
type UnifyContainerRow []string

const DefaultContainerRowLen int = 3
const DefaultContainerColLen int = 2

var unifyContainerRowPool = sync.Pool{
	New: func() interface{} {
		// fmt.Println("New is called for unifyContainerRowPool")
		conRow := make([]string, DefaultContainerColLen)
		conRow = resetUnifyContainerRow(conRow)
		return conRow[:0]
	},
}

func NewUnifyContainer() [][]string {
	con := unifyContainerPool.Get().([][]string)
	return con[:0]
}

func PutUnifyContainer(con [][]string) {
	for i := 0; i < len(con); i++ {
		PutUnifyContainerRow(con[i])
	}
	unifyContainerPool.Put(con)
}

func NewUnifyContainerRow() []string {
	con := unifyContainerRowPool.Get().([]string)
	return con[:0]
}

func getPtrSliceFromStrSlice(s []string) []interface{} {
	out := make([]interface{}, 0, len(s))
	for i := 0; i < len(s); i++ {
		out = append(out, &s[i])
	}
	return out
}

func PutUnifyContainerRow(con []string) {
	unifyContainerRowPool.Put(con)
}

var unifyContainerPool = sync.Pool{
	New: func() interface{} {
		// fmt.Println("New is called for unifyContainerPool")
		con := make([][]string, 0, DefaultContainerRowLen)
		return con
	},
}

func resetUnifyContainerRow(con []string) []string {
	for i := 0; i < len(con); i++ {
		con[i] = ""
	}
	return con
}
