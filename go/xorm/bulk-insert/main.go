package main

import (
	"fmt"
	"math/rand"

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

func getAuthors(e *xorm.Engine) []Author {
	authors := []Author{}
	must(e.Find(&authors))
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
	fmt.Println("author from db: ", authorsFromDB)
}
