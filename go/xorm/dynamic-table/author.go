package main

import (
	"fmt"
	"reflect"

	dstruct "github.com/goldeneggg/structil/dynamicstruct"
)

func BuildAuthor() interface{} {
	// Add fields using Builder with AddXXX method chain
	b := dstruct.NewBuilder().
		AddStringWithTag("Name", `xorm:"pk incr 'name'"`)

	// SetStructName sets the name of DynamicStruct
	// Note: Default struct name is "DynamicStruct"
	b.SetStructName("Author")

	// Build returns a DynamicStruct
	ds, err := b.Build()
	if err != nil {
		panic(err)
	}

	// Print struct definition with Definition method
	// Struct fields are automatically orderd by field name
	fmt.Println(ds.Definition())
	dsInt := ds.NewInterface()
	fmt.Printf("dsInt: %#v\n", dsInt)

	fmt.Printf("dsInt type: %v\n", reflect.TypeOf(dsInt))
	a := dsInt.(*Author)
	fmt.Println(a)

	// fmt.Printf("dsInt method: %v\n", reflect.ValueOf(dsInt).Method(0))
	return dsInt
	// return reflect.New(reflect.TypeOf(dsInt))
}
