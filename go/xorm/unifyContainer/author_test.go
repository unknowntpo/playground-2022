package main

import (
	"xorm.io/xorm"
)

func setup() *xorm.Engine {
	engine, err := xorm.NewEngine("sqlite3", ":memory:")
	must(err)

	must(engine.Sync(new(Author)))

	return engine
	// authors := makeAuthors()
	// fmt.Println("before insert", authors)
	// insertAuthors(engine, authors)

	// // get author by id
	// author3, err := GetAuthorByID(engine, int64(3))
	// must(err)
	// fmt.Println("author 3: ", showContent(author3))

	// authorBob, err := GetAuthorByName(engine, "Bob")
	// must(err)
	// fmt.Println("author bob: ", showContent(authorBob))
}

func reset(as *[]Author) {
	for i := 0; i < len(*as); i++ {
		(*as)[i] = Author{}
	}
}
