package main

import (
	"runtime"
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
		var m1, m2 runtime.MemStats
		runtime.GC()
		runtime.ReadMemStats(&m1)
		b.Log("alloc before func", m1.HeapAlloc)
		for i := 0; i < b.N; i++ {
			authors := []Author{}
			GetAllAuthors(engine, &authors)
		}
		runtime.ReadMemStats(&m2)
		b.Log("alloc diff after func", m2.HeapAlloc-m1.HeapAlloc)
	})
	b.ResetTimer()
	b.Run("with pool", func(b *testing.B) {
		for i := 0; i < b.N; i++ {
			var m1, m2 runtime.MemStats
			runtime.GC()
			runtime.ReadMemStats(&m1)

			authors := authorsPool.Get().([]Author)
			reset(&authors)
			GetAllAuthors(engine, &authors)

			runtime.ReadMemStats(&m2)
			b.Log("alloc diff after func", m2.HeapAlloc, m1.HeapAlloc)
			authorsPool.Put(authors)
		}
	})
}

func BenchmarkInsertAuthorsWithPool(b *testing.B) {
	b.ReportAllocs()

	engine := setup()

	authorsToBeInserted := makeAuthors()
	insertAuthors(engine, authorsToBeInserted)
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		authors := authorsPool.Get().([]Author)
		reset(&authors)
		GetAllAuthors(engine, &authors)
		authorsPool.Put(authors)
	}
}

func BenchmarkInsertAuthorsNoPool(b *testing.B) {
	b.ReportAllocs()

	engine := setup()

	authorsToBeInserted := makeAuthors()
	insertAuthors(engine, authorsToBeInserted)
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		authors := []Author{}
		GetAllAuthors(engine, &authors)
	}
}

func reset(as *[]Author) {
	for i := 0; i < len(*as); i++ {
		(*as)[i] = Author{}
	}
}
