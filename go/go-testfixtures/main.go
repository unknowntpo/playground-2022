package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"math/rand"
	"reflect"
	"time"

	fix "github.com/go-testfixtures/testfixtures/v3"

	"xorm.io/xorm"

	_ "github.com/mattn/go-sqlite3"
)

type Post struct {
	ID    int64  `xorm:"pk autoincr id", json:"id"`
	Title string `json:"title"`
	Text  string `json:"text"`
}

func (Post) Generate(r *rand.Rand, size int) reflect.Value {
	const alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01233456789"

	var buffer bytes.Buffer
	for i := 0; i < size; i++ {
		index := rand.Intn(len(alphabet))
		buffer.WriteString(string(alphabet[index]))
	}
	p := Post{
		Title: buffer.String(),
		Text:  buffer.String(),
	}

	fmt.Println("generated post: ", p)

	return reflect.ValueOf(p)
}

func generatePosts(num int) []interface{} {
	s := rand.NewSource(time.Now().Unix())
	r := rand.New(s)
	p := Post{}
	out := make([]interface{}, 0, num)
	for i := 0; i < num; i++ {
		pv := p.Generate(r, num)
		fmt.Printf("post: %T", pv.Interface())
		out = append(out, pv.Interface())
	}
	return out
}

func main() {
	e := setupEngine()
	setupFixtrues(e)
	res := [][]string{}
	err := e.SQL("select * from post").Find(&res)
	must(err)
	fmt.Println(ShowContent(res))
}

func setupEngine() *xorm.Engine {
	engine, err := xorm.NewEngine("sqlite3", ":memory:")
	must(err)
	must(engine.Sync(new(Post)))
	return engine
}

func setupFixtrues(e *xorm.Engine) {
	posts := generatePosts(10)
	fmt.Println(ShowContent(posts))
	loader, err := fix.New(
		fix.Template(),
		// the above options are optional
		fix.Database(e.DB().DB),
		fix.DangerousSkipTestDatabaseCheck(),
		fix.Dialect("sqlite3"),
		fix.TemplateDelims("{{", "}}"),
		// fix.TemplateOptions("missingkey=zero"),
		fix.TemplateData(posts),

		fix.Directory("./fixtures"),
	)
	must(err)
	must(loader.Load())
}

func must(e error) {
	if e != nil {
		panic(e)
	}
}

func ShowContent(i interface{}) string {
	b, _ := json.MarshalIndent(i, "", "\t")
	return string(b)
}
