package main

import (
	"testing"

	"xorm.io/xorm"
)

func BenchmarkContainer(b *testing.B) {
	engine, err := xorm.NewEngine("sqlite3", ":memory:")
	must(err)

	must(engine.Sync(new(Author)))

	authors := makeAuthors()

	batchNum := 1000
	for i := 0; i < batchNum; i++ {
		insertAuthors(engine, authors)
	}

	b.Run("StructureBinding", func(b *testing.B) {
		sess := engine.NewSession()
		defer sess.Close()
		must(sess.Begin())

		b.ResetTimer()
		for i := 0; i < b.N; i++ {
			con := GetAllAuthorsStructXorm(sess)
			must(err)
			_ = con
		}
	})

	b.Run("UnifyContainerWithPool", func(b *testing.B) {
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

	b.Run("UnifyContainerNoPool", func(b *testing.B) {
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
