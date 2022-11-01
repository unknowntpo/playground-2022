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

	rowNums := []int{1000, 10000, 100000, 1000000}

	for _, rowNum := range rowNums {
		authors := makeAuthors(rowNum)

		// avoid max arg limit
		step := 1000
		for i := 0; i < rowNum; i += step {
			insertAuthors(engine, authors[i:i+step])
			i += step
		}

		b.Run(fmt.Sprintf("StructureBinding-%v", rowNum), func(b *testing.B) {
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

		b.Run(fmt.Sprintf("UnifyContainerWithPool-%v", rowNum), func(b *testing.B) {
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

		b.Run(fmt.Sprintf("UnifyContainerNoPool-%v", rowNum), func(b *testing.B) {
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
}
