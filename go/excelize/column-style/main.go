package main

import (
	"fmt"
	"math/rand"

	"github.com/xuri/excelize/v2"
)

func main() {
	f := excelize.NewFile()
	for r := 1; r <= 4; r++ {
		row := []int{rand.Intn(100), rand.Intn(100), rand.Intn(100), rand.Intn(100)}
		if err := f.SetSheetRow("Sheet1", fmt.Sprintf("A%d", r), &row); err != nil {
			fmt.Println(err)
		}
	}

	must(setDiffLeft(f))
	must(setDiffRight(f))

	if err := f.SaveAs("Book1.xlsx"); err != nil {
		fmt.Println(err)
	}
}

func must(err error) {
	if err != nil {
		panic(err)
	}
}

func setDiffLeft(f *excelize.File) error {
	diffLeftStyle, err := f.NewStyle(DiffLeftColumnCellStyle)
	must(err)
	f.SetColStyle("Sheet1", "C", diffLeftStyle)
	return nil
}

func setDiffRight(f *excelize.File) error {
	diffLeftStyle, err := f.NewStyle(DiffRightColumnCellStyle)
	must(err)
	f.SetColStyle("Sheet1", "D", diffLeftStyle)
	return nil
}

const (
	DiffLeftColumnCellStyle = `
{
	"border":[
		{"type":"left","color":"#808080","style":1},
		{"type":"top","color":"#808080","style":1},
		{"type":"right","color":"#808080","style":1},
		{"type":"bottom","color":"#808080","style":1}
	],
	"font": {"family":"calibri", "fontSize":12, "color":"#000000"},
	"alignment": {"horizontal":"center", "vertical":"center", "wrap_text":true},
	"fill": {"type":"pattern","color":["#FAB1AF"],"pattern":1}
}`

	DiffRightColumnCellStyle = `
{
	"border":[
		{"type":"left","color":"#808080","style":1},
		{"type":"top","color":"#808080","style":1},
		{"type":"right","color":"#808080","style":1},
		{"type":"bottom","color":"#808080","style":1}
	],
	"font": {"family":"calibri", "fontSize":12, "color":"#000000"},
	"alignment": {"horizontal":"center", "vertical":"center", "wrap_text":true},
	"fill": {"type":"pattern","color":["#DAF7A6"],"pattern":1}
}`
)
