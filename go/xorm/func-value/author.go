package main

import (
	"fmt"
	"sync"

	"xorm.io/xorm"
)

type Author struct {
	ID   int64 `xorm:"pk autoincr"`
	Name string
}

func GetAuthorByID(e xorm.Interface, id int64) (*Author, error) {
	f := func() *Author {
		return &Author{ID: id}
	}
	return getAuthor(e, f)
}

func GetAllAuthors(e xorm.Interface, slice *[]Author) {
	err := e.Find(slice)
	must(err)
}

func GetAuthorByName(e xorm.Interface, name string) (*Author, error) {
	f := func() *Author {
		return &Author{Name: name}
	}
	return getAuthor(e, f)
}

func getAuthor(e xorm.Interface, optFn func() *Author) (*Author, error) {
	// apply option
	a := optFn()
	has, err := e.Get(a)
	if err != nil {
		return nil, fmt.Errorf("faile on e.Get: %v", err)
	}
	if !has {
		return nil, fmt.Errorf("author not found")
	}
	return a, nil
}

const num = 10000

func makeAuthors() []Author {
	authors := []Author{}
	names := []string{"Alice", "Bob", "Ally"}
	for i := 0; i < num; i++ {
		authors = append(authors, Author{Name: names[i%3]})
	}

	return authors
}

func insertAuthors(e *xorm.Engine, authors []Author) {
	_, err := e.Insert(authors)
	must(err)
}

var authorsPool = sync.Pool{
	New: func() interface{} {
		fmt.Println("New is called")
		return []Author{}
	},
}
