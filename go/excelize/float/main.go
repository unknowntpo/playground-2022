package main

import (
	"fmt"

	excelize "github.com/xuri/excelize/v2"
)

func main() {
	f := excelize.NewFile()
	// Create a new worksheet.
	index := f.NewSheet("Sheet2")
	// Set value of a cell.
	f.SetCellValue("Sheet2", "A2", "Hello world.")
	f.SetCellValue("Sheet2", "A3", "0.1234566789")

	// Set the active worksheet of the workbook.
	f.SetActiveSheet(index)
	// Save the spreadsheet by the given path.
	if err := f.SaveAs("Book1.xlsx"); err != nil {
		fmt.Println(err)
	}

	ff, err := excelize.OpenFile("Book1.xlsx")
	must(err)
	_ = ff
}

func must(err error) {
	if err != nil {
		panic(err)
	}
}
