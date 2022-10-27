package main

import (
	"encoding/json"
	"fmt"
	"os"
	"runtime/pprof"

	_ "github.com/mattn/go-sqlite3"
	"xorm.io/xorm"
)

func must(err error) {
	if err != nil {
		panic(err)
	}
}

func main() {
	cpuF, err := os.Create("cpu.prof")
	defer cpuF.Close()
	must(err)

	pprof.StartCPUProfile(cpuF)
	defer pprof.StopCPUProfile()

	heapF, err := os.Create("heap.prof")
	defer heapF.Close()
	pprof.WriteHeapProfile(heapF)

	engine, err := xorm.NewEngine("sqlite3", ":memory:")
	must(err)

	must(engine.Sync(new(Author)))

	authors := makeAuthors()
	// fmt.Println("before insert", authors)
	insertAuthors(engine, authors)

	// get author by id
	author3, err := GetAuthorByID(engine, int64(3))
	must(err)
	fmt.Println("author 3: ", showContent(author3))

	authorBob, err := GetAuthorByName(engine, "Bob")
	must(err)
	fmt.Println("author bob: ", showContent(authorBob))

	sess := engine.NewSession()
	defer sess.Close()
	must(sess.Begin())

	round := 10
	for i := 0; i < round; i++ {
		con := GetAllAuthorsStrSliceStdSQL(sess)
		must(err)
		fmt.Println("authors: ", showContent(con))
		PutUnifyContainer(con)
	}
}

func showContent(v interface{}) string {
	b, _ := json.MarshalIndent(v, "", "\t")
	return string(b)
}
