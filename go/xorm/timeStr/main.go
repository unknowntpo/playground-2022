package main

import (
	"fmt"
	"time"

	"xorm.io/xorm"

	_ "github.com/mattn/go-sqlite3"
)

type Toy struct {
	Name       string
	Age        int
	UploadDate time.Time
}

func must(err error) {
	if err != nil {
		panic(err)
	}
}

func setupEngine() *xorm.Engine {
	engine, err := xorm.NewEngine("sqlite3", ":memory:")
	must(err)
	must(engine.Sync(new(Toy)))
	return engine
}

func makeToys(count int) []interface{} {
	toys := make([]interface{}, 0, count)
	for i := 0; i < count; i++ {
		toys = append(toys, Toy{Name: "Bla", Age: 3, UploadDate: time.Now()})
	}
	return toys
}

func main() {
	x := setupEngine()

	toys := makeToys(1000)

	x.Insert(&toys)

	cons := &[][]string{}
	must(x.SQL("SELECT * FROM toy").Find(cons))

	fmt.Println("toys", cons)

	for _, con := range *cons {
		for i, c := range con {
			if i == 2 && c == "" {
				panic(c)
			}
		}
	}
}
