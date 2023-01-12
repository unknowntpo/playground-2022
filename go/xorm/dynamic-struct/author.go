package main

import (
	"fmt"
	"reflect"

	// dstruct "github.com/goldeneggg/structil/dynamicstruct"
	dstruct "github.com/ompluscator/dynamic-struct"
	"xorm.io/xorm"
)

// var DAuthor = BuildAuthor()

func BuildAuthor() interface{} {
	// Add fields using Builder with AddXXX method chain

	b := dstruct.NewStruct().
		AddField("ID", int64(0), `xorm:"id pk autoincr" json:"id,omitempty"`).
		AddField("Name", "", `xorm:"name text" json:"name,omitempty"`).
		Build().
		New()

	// SetStructName sets the name of DynamicStruct
	// Note: Default struct name is "DynamicStruct"
	// b.SetStructName("author")

	// // Build returns a DynamicStruct
	// ds, err := b.Build()
	// if err != nil {
	// 	panic(err)
	// }

	// fmt.Println("isPtr:", ds.IsPtr())

	// Print struct definition with Definition method
	// Struct fields are automatically orderd by field name
	// fmt.Println(ds.Definition())
	// dsInt := ds.NewInterface()
	fmt.Printf("dsInt: %#v\n", b)

	return b
}

func getAuthorsWithDynStruct(e *xorm.Engine) []any {
	authors := []any{}
	// must(e.Find(&authors))

	// sql := "SELECT COUNT(*) OVER() AS totalCount, * FROM author"
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

		// fieldPtrs = append(fieldPtrs, &totalCount)
		val := reflect.Indirect(reflect.ValueOf(author))

		for i := 0; i < val.NumField(); i++ {
			// fieldPtrs = append(fieldPtrs, getInterfacePtr(val.Field(i).Addr().Interface()).Interface())
			fieldPtrs = append(fieldPtrs, (val.Field(i).Addr().Interface()))
		}

		// debugType(fieldPtrs)
		cols, _ := rows.Columns()
		fmt.Println("columns", cols)

		must(rows.Scan(fieldPtrs...))

		fmt.Printf("author dynstruct: %#v\n", author)

		fmt.Printf("type of author dynstruct: %v\n", reflect.ValueOf(author).Elem().Kind())

		// must(rows.Scan(&totalCount, fieldPtrs...))
		// must(rows.Scan(&author))
		authors = append(authors, reflect.ValueOf(author).Elem().Interface())
	}
	fmt.Println("authors", authors)
	return authors
}

func getInterfacePtr(v any) reflect.Value {
	p := reflect.New(reflect.TypeOf(v))
	p.Elem().Set(reflect.ValueOf(v))
	return p
}

func debugType(arr []any) {
	arrVal := reflect.ValueOf(arr)
	for i := 0; i < arrVal.Len(); i++ {
		fmt.Printf("Kind of arr[%d]: %v, Type: [%v]\n", i, arrVal.Index(i).Elem().Kind(), arrVal.Index(i).Type().Name())
	}
}
