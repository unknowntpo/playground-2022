package model

import (
	"fmt"
	"os"
)

func Setup() error {
	wd, err := os.Getwd()
	if err != nil {
		return fmt.Errorf("failed to open file: %v", err)
	}
	fmt.Println("wd in model.Setup: %v", wd)

	f, err := os.Open("../../.env")
	// This will fail if you call Setup() in api package
	// because the in api pkg, env file is at ../.env
	if err != nil {
		return fmt.Errorf("failed to open file: %v", err)
	}
	fmt.Println("filename: ", f.Name())
	return nil
}
