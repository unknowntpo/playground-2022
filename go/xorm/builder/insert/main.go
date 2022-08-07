package main

import (
	"fmt"
	"reflect"

	"xorm.io/builder"
)

type Toy struct {
	Name string
	Age  int
}

// objs should contain same object
func makeEq(objs []interface{}) builder.Eq {
	eq := builder.Eq{}

	for _, o := range objs {
		oVal := reflect.ValueOf(o)
		for i := 0; i < oVal.NumField(); i++ {
			fVal := oVal.Field(i)
			fieldName := oVal.Type().Field(i).Name

			insertVals, ok := eq[fieldName]
			if !ok {
				// insertVals does not exist, create one.
				insertVals = []interface{}{}
				eq[fieldName] = insertVals
			}
			eq[fieldName] = append(insertVals.([]interface{}), fmt.Sprintf("%v", fVal.Interface()))
		}
		fmt.Println(eq)
	}

	return eq
}

func must(err error) {
	if err != nil {
		panic(err)
	}
}

func main() {
	toys := []interface{}{
		Toy{Name: "Bla", Age: 3},
		Toy{Name: "Foo", Age: 4},
		Toy{Name: "Ma", Age: 6},
	}
	eq := makeEq(toys)
	fmt.Println(eq)
	sql, args, err := builder.Insert(eq).Into("table1").ToSQL()
	must(err)

	fmt.Println("sql: ", sql)
	fmt.Println("args: ", args)
}
