package main

import (
	"encoding/json"
	"fmt"
	"math/rand"
	"reflect"

	_ "github.com/mattn/go-sqlite3"
	"xorm.io/xorm"
)

func must(err error) {
	if err != nil {
		panic(err)
	}
}

type Author struct {
	Name string
}

var letters = []rune("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")

func randSeq(n int) string {
	b := make([]rune, n)
	for i := range b {
		b[i] = letters[rand.Intn(len(letters))]
	}
	return string(b)
}

func getRandName() string {
	return randSeq(3)
}

func makeAuthors() []Author {
	authors := []Author{}
	for i := 0; i < 10; i++ {
		authors = append(authors, Author{Name: getRandName()})
	}

	return authors
}

func insertAuthors(e *xorm.Engine, authors []Author) {
	_, err := e.Insert(authors)
	must(err)
}

func getAuthors(e *xorm.Engine) []*Author {
	authors := []*Author{}
	// must(e.Find(&authors))

	sql := "SELECT COUNT(*) OVER() AS totalCount, * FROM author"
	rows, err := e.SQL(sql).Rows(&Author{})
	must(err)
	// SELECT * FROM author
	defer rows.Close()

	var totalCount int64
	_ = totalCount
	for rows.Next() {
		author := &Author{}
		// author := new(Author)
		fieldPtrs := []interface{}{}

		fieldPtrs = append(fieldPtrs, &totalCount)

		val := reflect.Indirect(reflect.ValueOf(author))

		fmt.Println("val can address", val.CanAddr())
		for i := 0; i < val.NumField(); i++ {
			fieldVal := val.Field(i)
			fmt.Println("can address", fieldVal.CanAddr())

			fieldPtrs = append(fieldPtrs, val.Field(i).Addr().Interface())
		}

		must(rows.Scan(fieldPtrs...))

		// must(rows.Scan(&totalCount, fieldPtrs...))
		// must(rows.Scan(&author))
		authors = append(authors, author)
	}
	fmt.Println("totalCount", totalCount)
	return authors
}

func main() {
	engine, err := xorm.NewEngine("sqlite3", "./test.db")
	must(err)

	must(engine.Sync(new(Author)))

	authors := makeAuthors()
	fmt.Println("before insert", authors)
	insertAuthors(engine, authors)

	authorsFromDB := getAuthors(engine)
	fmt.Println("author from db: ", showContent(authorsFromDB))
}

func showContent(v interface{}) string {
	b, _ := json.MarshalIndent(v, "", "\t")
	return string(b)
}
