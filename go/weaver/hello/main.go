package main

import (
	"context"
	"fmt"
	"log"

	"github.com/ServiceWeaver/weaver"
)

func main() {
	// Initialize the Service Weaver application.
	ctx := context.Background()
	root := weaver.Init(ctx)

	// Get a client to the Reverser component.
	reverser, err := weaver.Get[Reverser](root)
	if err != nil {
		log.Fatal(err)
	}

	// Call the Reverse method.
	reversed, err := reverser.Reverse(ctx, "!dlroW ,olleH")
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println(reversed)
}
