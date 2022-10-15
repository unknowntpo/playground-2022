package main

import (
	"encoding/json"
	"fmt"

	_ "github.com/mattn/go-sqlite3"
	"xorm.io/xorm"
)

func must(err error) {
	if err != nil {
		panic(err)
	}
}

func main() {
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

	strSlice := [][]string{}
	GetAllAuthorsStrSlice(engine, &strSlice)
	must(err)
	fmt.Println("authors: ", showContent(strSlice))

}

func showContent(v interface{}) string {
	b, _ := json.MarshalIndent(v, "", "\t")
	return string(b)
}
