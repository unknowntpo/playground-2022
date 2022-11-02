package main

import (
	"fmt"
	"reflect"
	"sync"

	"xorm.io/xorm"

	"xorm.io/builder"
)

type Author struct {
	ID   int64 `xorm:"id pk autoincr"`
	Name string
	Col0 string
	Col1 string
	Col2 string
	Col3 string
	Col4 string
	Col5 string
	Col6 string
	Col7 string
	Col8 string
}

func (a Author) TableName() string {
	return "author"
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

func GetAllAuthorsStrSliceStdSQL(e xorm.Interface) [][]string {
	sess := e.(*xorm.Session)
	b := builder.Dialect("sqlite3").Select("*").From("author")
	sql, err := b.ToBoundSQL()

	// rows, err := e.SQL(b).Rows()
	// must(err)
	rows, err := sess.Tx().Query(sql)
	must(err)
	// rows, err := sess.SQL(b).Query()
	// must(err)
	con := NewUnifyContainer()
	for rows.Next() {
		conRow := NewUnifyContainerRow()
		// append to len equal to fields num in Author
		authorFieldNum := reflect.ValueOf(Author{}).NumField()
		for i := 0; i < authorFieldNum; i++ {
			conRow = append(conRow, "")
		}

		// conRowIntSlice := strSliceToInterfaceSlice(conRow)
		conRowPtrSlice := getPtrSliceFromStrSlice(conRow)
		must(rows.Scan(conRowPtrSlice...))
		con = append(con, conRow)
	}

	return con
}

func GetAllAuthorsStrSliceXorm(e xorm.Interface, slice *[][]string) [][]string {
	obj := Author{}
	tableName := reflect.ValueOf(obj).Type().Name()
	b := builder.Dialect("sqlite3").Select("*").From(tableName)

	err := e.SQL(b).Find(slice)
	must(err)

	return *slice
}

func GetAllAuthorsStructXorm(e xorm.Interface) []Author {
	objs := []Author{}
	b := builder.Dialect("sqlite3").Select("*").From(Author{}.TableName())

	err := e.SQL(b).Find(&objs)
	must(err)

	return objs
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

// const num = 10000

func makeAuthors(num int) []Author {
	authors := []Author{}
	names := []string{"Alice", "Bob", "Ally"}
	for i := 0; i < num; i++ {
		authors = append(authors, Author{
			Name: names[i%3],
			Col0: names[i%3],
			Col1: names[i%3],
			Col2: names[i%3],
			Col3: names[i%3],
			Col4: names[i%3],
			Col5: names[i%3],
			Col6: names[i%3],
			Col7: names[i%3],
			Col8: names[i%3],
		})
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
		// slice := make([]Author, 0, num)
		// return &slice
		return make([]Author, 0, 10000)
	},
}

var authorPool = sync.Pool{
	New: func() interface{} {
		fmt.Println("New is called")
		return new(Author)
	},
}
