package main

import (
	"fmt"
	"testing"

	"xorm.io/xorm"
)

func BenchmarkContainer(b *testing.B) {
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

	b.Run("withPool", func(b *testing.B) {
		sess := engine.NewSession()
		defer sess.Close()
		must(sess.Begin())

		b.ResetTimer()
		for i := 0; i < b.N; i++ {
			con := GetAllAuthorsStrSliceStdSQL(sess)
			must(err)
			_ = con
			// fmt.Println("authors: ", showContent(con))
			PutUnifyContainer(con)
		}
	})

	b.Run("noPool", func(b *testing.B) {
		sess := engine.NewSession()
		defer sess.Close()
		must(sess.Begin())

		b.ResetTimer()
		for i := 0; i < b.N; i++ {
			slice := &[][]string{}
			con := GetAllAuthorsStrSliceXorm(sess, slice)
			must(err)
			_ = con
			// fmt.Println(len(con))
		}
	})

}
