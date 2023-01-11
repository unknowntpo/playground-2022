package main

import (
	"fmt"
	"reflect"

	dstruct "github.com/goldeneggg/structil/dynamicstruct"
	"xorm.io/xorm"
)

// var DAuthor = BuildAuthor()

func BuildAuthor() interface{} {
	// Add fields using Builder with AddXXX method chain
	b := dstruct.NewBuilder().
		AddIntWithTag("ID", `xorm:"pk autoincr 'id'" json:"id,omitempty"`).
		AddStringWithTag("Name", `xorm:"'name'"`)

	// SetStructName sets the name of DynamicStruct
	// Note: Default struct name is "DynamicStruct"
	b.SetStructName("author")

	// Build returns a DynamicStruct
	ds, err := b.Build()
	if err != nil {
		panic(err)
	}

	// fmt.Println("isPtr:", ds.IsPtr())

	// Print struct definition with Definition method
	// Struct fields are automatically orderd by field name
	// fmt.Println(ds.Definition())
	dsInt := ds.NewInterface()
	// fmt.Printf("dsInt: %#v\n", dsInt)

	return dsInt
}

func getAuthorsWithDynStruct(e *xorm.Engine) []any {
	authors := []any{}
	// must(e.Find(&authors))

	sql := "SELECT * FROM author"

	sess := e.NewSession()
	must(sess.Begin())
	defer sess.Close()

	rows, err := sess.Tx().Query(sql)
	must(err)

	var totalCount int64
	_ = totalCount
	for rows.Next() {
		author := BuildAuthor()
		// author := new(Author)
		fieldPtrs := []any{}

		fieldPtrs = append(fieldPtrs, &totalCount)
		val := reflect.Indirect(reflect.ValueOf(author))

		for i := 0; i < val.NumField(); i++ {
			fieldPtrs = append(fieldPtrs, val.Field(i).Addr().Interface())
		}

		must(rows.Scan(fieldPtrs))

		// must(rows.Scan(&totalCount, fieldPtrs...))
		// must(rows.Scan(&author))
		authors = append(authors, author)
	}
	fmt.Println("totalCount", totalCount)
	return authors
}
