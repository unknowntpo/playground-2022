package main

import (
	"encoding/json"
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

func makeAuthors(needInit bool) []interface{} {
	d := `{"name": "hello"}`
	authors := []interface{}{}
	for i := 0; i < 10; i++ {
		author := BuildAuthor()
		if needInit {
			must(json.Unmarshal([]byte(d), author))
		}

		fmt.Println("author:", showContent(author))
		authors = append(authors, author)
	}
	showContent(authors)
	// s, _ := json.MarshalIndent(authors, "", "\t")
	// fmt.Println(string(s))
	return authors
}

func insertAuthors(e *xorm.Engine, authors []interface{}) {
	_, err := e.Table("author").Insert(&authors)
	must(err)
}

func getAuthors(e *xorm.Engine) interface{} {
	// authors := []*Author{}
	// dAuthorVal := reflect.ValueOf(BuildAuthor())
	// authors := reflect.MakeSlice(dAuthorVal.Type(), 0, 0).Interface()
	// authors := makeAuthors(false)
	authors := []interface{}{}

	// must(e.Find(&authors))

	sql := "SELECT COUNT(*) OVER() AS totalCount, * FROM author"
	rows, err := e.SQL(sql).Rows(BuildAuthor())
	must(err)
	defer rows.Close()

	var totalCount int64
	_ = totalCount
	for rows.Next() {
		a := BuildAuthor()
		must(rows.Scan(a))
		authors = append(authors, a)
	}

	// 	fmt.Println("val can address", val.CanAddr())
	// 	for i := 0; i < val.NumField(); i++ {
	// 		fieldVal := val.Field(i)
	// 		fmt.Println("can address", fieldVal.CanAddr())

	// 		fieldPtrs = append(fieldPtrs, val.Field(i).Addr().Interface())
	// 	}

	// 	must(rows.Scan(fieldPtrs...))

	// 	// must(rows.Scan(&totalCount, fieldPtrs...))
	// 	// must(rows.Scan(&author))
	// 	authors = append(authors, author)
	// }
	// fmt.Println("totalCount", totalCount)
	return authors
}

func main() {
	engine, err := xorm.NewEngine("sqlite3", ":memory:")
	must(err)
	engine.ShowSQL(true)

	_, err = engine.Exec("CREATE TABLE author (id integer primary key autoincrement, name varchar(255));")
	must(err)

	// DAuthor := BuildAuthor()

	// fmt.Printf(">>>DAuthor: %#v\n", DAuthor)

	// fmt.Printf(">>>DAuthor: %#v\n", DAuthor.TableName())

	// must(engine.Sync(&DAuthor))

	authors := makeAuthors(true)
	fmt.Println("before insert", authors)
	insertAuthors(engine, authors)

	authorsFromDB := getAuthors(engine)
	fmt.Println("author from db: ", showContent(authorsFromDB))
}

func showContent(v interface{}) string {
	b, _ := json.MarshalIndent(v, "", "\t")
	return string(b)
}
