package main

import (
	"fmt"
	"reflect"
	"strings"

	dstruct "github.com/goldeneggg/structil/dynamicstruct"
)

type TableContainer struct {
	Table interface{}
}

func (t *TableContainer) TableName() string {
	fmt.Println("in tableName", reflect.ValueOf(t.Table))
	fmt.Printf("tCon inside TableName: %#v\n", t)
	return reflect.ValueOf(t.Table).Elem().FieldByName("TableName").String()
}

func InitTableContainer(tableName string, tableStruct interface{}) TableContainer {
	tCon := TableContainer{Table: tableStruct}
	// reflect.Indirect(reflect.ValueOf(tCon.Table)).

	reflect.ValueOf(tCon.Table).Elem().
		FieldByName("TableName").
		Set(reflect.ValueOf(tableName))
	// fmt.Printf("%#v\n", tCon.Table)
	// fmt.Printf("tCon.Table%#v\n", tCon.Table)
	fmt.Printf("inside InitTableContainer, call tCon.TableName(): %v\n", tCon.TableName())

	fmt.Printf("tCon inside initTableCOntainer: %#v\n", tCon)

	return tCon
}

var DAuthor = BuildAuthor()

func BuildAuthor() TableContainer {
	// Add fields using Builder with AddXXX method chain
	b := dstruct.NewBuilder().
		AddStringWithTag("Name", `xorm:"pk incr 'name'"`).
		AddStringWithTag("TableName", `json:"-"`)

	// SetStructName sets the name of DynamicStruct
	// Note: Default struct name is "DynamicStruct"
	b.SetStructName("Author")

	// Build returns a DynamicStruct
	ds, err := b.Build()
	if err != nil {
		panic(err)
	}

	fmt.Println("isPtr:", ds.IsPtr())

	// Print struct definition with Definition method
	// Struct fields are automatically orderd by field name
	fmt.Println(ds.Definition())
	dsInt := ds.NewInterface()
	fmt.Printf("dsInt: %#v\n", dsInt)

	fmt.Printf("dsInt type: %v\n", reflect.TypeOf(dsInt))

	// fmt.Printf("dsInt method: %v\n", reflect.ValueOf(dsInt).Method(0))
	return InitTableContainer(strings.ToLower("Author"), dsInt)
}
