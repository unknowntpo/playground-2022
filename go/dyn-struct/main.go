package main

import (
	"fmt"
	"reflect"
)

type Foo struct {
	A     int
	B     string
	Table interface{}
}

func (f Foo) TableName() string {
	c := reflect.ValueOf(f.Table).Elem()
	fmt.Println("numField", c.NumField())
	cTyp := c.Type()
	for i := 0; i < c.NumField(); i++ {
		fmt.Println("field", cTyp.Field(i).Name)
	}
	return c.FieldByName("Name").String()
}

func main() {
	foo := Foo{A: 2, B: "hello foo"}
	foo2 := reflect.StructOf([]reflect.StructField{
		reflect.StructField{
			Name:      "Name",
			Anonymous: false,
			Type:      reflect.TypeOf("hello"),
		},
	})
	foo2Val := reflect.New(foo2)
	customTableName := "myTableName"
	foo2Val.Elem().FieldByName("Name").Set(reflect.ValueOf(customTableName))
	foo.Table = foo2Val.Interface()
	fmt.Println("foo", foo)

	//this should really fail, too. But it doesn't. And it returns back a very weird response.
	fmt.Println("foo.TableName()", foo.TableName())
}
