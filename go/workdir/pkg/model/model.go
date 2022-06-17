package model

import (
	"fmt"
	"os"
	"path/filepath"
	"runtime"
)

func Pwd() string {
	_, f, _, _ := runtime.Caller(1)
	return filepath.Dir(f)
}

func Setup() error {
	wd, err := os.Getwd()
	if err != nil {
		return fmt.Errorf("failed to open file: %v", err)
	}
	fmt.Printf("wd in model.Setup: %v\n", wd)

	pwd := Pwd()

	f, err := os.Open(filepath.Join(pwd, "../../.env"))
	// This will fail if you call Setup() in api package
	// because the in api pkg, env file is at ../.env
	if err != nil {
		return fmt.Errorf("failed to open file: %v", err)
	}
	fmt.Println("filename: ", f.Name())
	return nil
}
