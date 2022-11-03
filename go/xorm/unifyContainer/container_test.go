package main

import (
	"fmt"
	"runtime"
	"testing"

	"xorm.io/xorm"
)

func BenchmarkContainer(b *testing.B) {
	// 8 worker do 16 jobs
	const jobNum = 16
	const workerNum = 8

	jobChan := make(chan func(b *testing.B), jobNum)
	doneChan := make(chan struct{}, jobNum)

	worker := func(b *testing.B) {
		for j := range jobChan {
			j(b)
			// when worker complete job, it send signal to doneChan
			doneChan <- struct{}{}
		}
	}

	// start workers
	for i := 0; i < workerNum; i++ {
		go worker(b)
	}

	// Init engine
	// engine, err := xorm.NewEngine("sqlite3", ":memory:")
	engine, err := xorm.NewEngine("sqlite3", "test.db")

	must(err)

	must(engine.Sync(new(Author)))

	rowNums := []int{1000, 10000, 100000, 200000, 500000, 1000000}

	for _, rowNum := range rowNums {
		authors := makeAuthors(rowNum)

		// avoid max arg limit
		step := 1000
		for i := 0; i < rowNum; i += step {
			insertAuthors(engine, authors[i:i+step])
			i += step
		}

		b.Run(fmt.Sprintf("StructureBinding-%v", rowNum), func(b *testing.B) {
			job := func(b *testing.B) {
				sess := engine.NewSession()
				defer sess.Close()
				must(sess.Begin())
				con := GetAllAuthorsStructXorm(sess)
				must(err)
				_ = con
			}
			b.ResetTimer()
			for i := 0; i < b.N; i++ {
				// send jobs to worker
				for i := 0; i < jobNum; i++ {
					jobChan <- job
				}
				// wait for all jobs to complete
				for i := 0; i < jobNum; i++ {
					<-doneChan
				}
			}
			cleanup(engine)
		})

		b.Run(fmt.Sprintf("UnifyContainerWithPool-%v", rowNum), func(b *testing.B) {
			job := func(b *testing.B) {
				sess := engine.NewSession()
				defer sess.Close()
				must(sess.Begin())
				con := GetAllAuthorsStrSliceStdSQL(sess)
				must(err)
				_ = con
				// fmt.Println("authors: ", showContent(con))
				PutUnifyContainer(con)
			}
			b.ResetTimer()
			for i := 0; i < b.N; i++ {
				// send jobs to worker
				for i := 0; i < jobNum; i++ {
					jobChan <- job
				}
				// wait for all jobs to complete
				for i := 0; i < jobNum; i++ {
					<-doneChan
				}
			}
			cleanup(engine)
		})

		b.Run(fmt.Sprintf("UnifyContainerNoPool-%v", rowNum), func(b *testing.B) {
			job := func(b *testing.B) {
				sess := engine.NewSession()
				defer sess.Close()
				must(sess.Begin())
				slice := &[][]string{}
				con := GetAllAuthorsStrSliceXorm(sess, slice)
				must(err)
				_ = con
			}
			b.ResetTimer()
			for i := 0; i < b.N; i++ {
				// send jobs to worker
				for i := 0; i < jobNum; i++ {
					jobChan <- job
				}
				// wait for all jobs to complete
				for i := 0; i < jobNum; i++ {
					<-doneChan
				}
			}
			cleanup(engine)
		})
	}
}

func resetDB(e *xorm.Engine) {
	_, err := e.Exec("DELETE FROM author")
	must(err)
}

func cleanup(e *xorm.Engine) {
	runtime.GC()
	resetDB(e)
}
