package main

import (
	"fmt"
	"runtime"
	"testing"

	_ "github.com/mattn/go-sqlite3"

	"xorm.io/xorm"
)

func BenchmarkContainer(b *testing.B) {
	// 8 worker do 16 jobs
	const jobNum = 16
	const workerNum = 8

	jobChan := make(chan func(b *testing.B), jobNum)
	doneChan := make(chan struct{}, jobNum)

	worker := func(b *testing.B) {
		// b.Helper()
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
	// engine, err := xorm.NewEngine("sqlite3", ":memory:?cache=shared")
	engine, err := xorm.NewEngine("sqlite3", "test.db")
	must(err)

	must(engine.Sync(new(Author)))

	// if we use file db, we need to make sure that author table is empty
	resetDB(engine)

	rowNums := []int{1000, 10000, 100000, 200000, 500000, 1000000}

	for _, rowNum := range rowNums {
		authors := makeAuthors(rowNum)

		// avoid max arg limit
		step := 1000
		for i := 0; i < rowNum; i += step {
			insertAuthors(engine, authors[i:i+step])
			fmt.Println("row num:", rowNum, "steps", step)
			i += step
		}

		b.Run(fmt.Sprintf("StructureBinding-%v", rowNum), func(b *testing.B) {
			job := func(b *testing.B) {
				sess := engine.NewSession()
				defer sess.Close()
				must(sess.Begin())
				con := GetAllAuthorsStructXorm(sess)
				must(err)
				assertEqual(b, len(con), rowNum)
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
			runtime.GC()
		})

		b.Run(fmt.Sprintf("UnifyContainerWithPool-%v", rowNum), func(b *testing.B) {
			job := func(b *testing.B) {
				sess := engine.NewSession()
				defer sess.Close()
				must(sess.Begin())
				con := GetAllAuthorsStrSliceStdSQL(sess)
				must(err)
				assertEqual(b, len(con), rowNum)
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
			runtime.GC()
		})

		b.Run(fmt.Sprintf("UnifyContainerNoPool-%v", rowNum), func(b *testing.B) {
			job := func(b *testing.B) {
				sess := engine.NewSession()
				defer sess.Close()
				must(sess.Begin())
				slice := &[][]string{}
				con := GetAllAuthorsStrSliceXorm(sess, slice)
				must(err)
				assertEqual(b, len(con), rowNum)
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
			runtime.GC()
		})
		resetDB(engine)
	}
}

func assertEqual(b *testing.B, left int, right int) {
	if left != right {
		panic(fmt.Sprintf("left and right is not equal: left = [%v], right = [%v]", left, right))
	}
}

func resetDB(e *xorm.Engine) {
	_, err := e.Exec("DELETE FROM author")
	must(err)
}
