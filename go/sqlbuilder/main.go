package main

import (
	"fmt"
	"reflect"

	builder "github.com/huandu/go-sqlbuilder"
)

type Card struct {
	Name string
	Type string
}

func (c Card) Cols() []string {
	cVal := reflect.ValueOf(c)
	cTyp := cVal.Type()
	cols := make([]string, 0, cTyp.NumField())
	for i := 0; i < cTyp.NumField(); i++ {
		col := cTyp.Field(i).Name
		cols = append(cols, col)
	}
	return cols
}

func (c Card) Vals() []interface{} {
	cVal := reflect.ValueOf(c)
	vals := make([]interface{}, 0, cVal.NumField())
	for i := 0; i < cVal.NumField(); i++ {
		val := cVal.Field(i).Interface()
		vals = append(vals, val)
	}
	return vals
}

func main() {
	ib := builder.NewInsertBuilder()
	ib.InsertInto("demo.user")
	cards := []Card{
		{Name: "123", Type: "black"},
		{Name: "456", Type: "red"},
	}
	ib.Cols(cards[0].Cols()...)
	ib = fillValues(ib, cards)

	sql, args := ib.Build()
	fmt.Println(sql)
	fmt.Println(args)
}

func must(err error) {
	if err != nil {
		panic(err)
	}
}

func fillValues(ib *builder.InsertBuilder, cards []Card) *builder.InsertBuilder {
	for _, c := range cards {
		vals := c.Vals()
		ib = ib.Values(vals...)
	}
	return ib
}
