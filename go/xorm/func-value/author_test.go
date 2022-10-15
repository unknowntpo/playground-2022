package main

import (
	"testing"

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

func BenchmarkInsertAuthors(b *testing.B) {
	b.ReportAllocs()

	engine := setup()

	authorsToBeInserted := makeAuthors()
	insertAuthors(engine, authorsToBeInserted)
	b.ResetTimer()
	b.Run("no pool", func(b *testing.B) {
		for i := 0; i < b.N; i++ {
			authors := []Author{}
			GetAllAuthors(engine, &authors)
		}
	})
	b.ResetTimer()
	b.Run("with pool", func(b *testing.B) {
		for i := 0; i < b.N; i++ {
			authors := authorsPool.Get().([]Author)
			defer authorsPool.Put(authors)
			GetAllAuthors(engine, &authors)
		}
	})
}
