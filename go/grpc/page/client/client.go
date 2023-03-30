package main

import (
	"context"
	"io"
	"log"

	"github.com/unknowntpo/playground-2022/go/grpc/page"

	"google.golang.org/grpc"
)

func main() {
	conn, err := grpc.Dial("localhost:50051", grpc.WithInsecure())
	if err != nil {
		log.Fatalf("Failed to dial server: %v", err)
	}
	defer conn.Close()

	client := page.NewPageServiceClient(conn)

	// Call GetHead to get the head page key
	getHeadReq := &page.GetHeadRequest{
		ListKey: "mylist",
		UserId:  123,
	}
	getHeadStream, err := client.GetHead(context.Background(), getHeadReq)
	if err != nil {
		log.Fatalf("Failed to get head page key: %v", err)
	}
	pageKey, err := getHeadStream.Recv()
	if err == io.EOF {
		log.Println("No page key received")
	} else if err != nil {
		log.Fatalf("Failed to receive page key: %v", err)
	} else {
		log.Printf("Received page key: %s", pageKey.Key)
	}

	// Call GetPage to get the pages
	getPageStream, err := client.GetPage(context.Background())
	if err != nil {
		log.Fatalf("Failed to get pages: %v", err)
	}
	if err := getPageStream.Send(&PageKey{Key: "123"}); err != nil {
		log.Fatalf("Failed to send page key: %v", err)
	}
	page, err := getPageStream.Recv()
	if err == io.EOF {
		log.Println("No page received")
	} else if err != nil {
		log.Fatalf("Failed to receive page: %v", err)
	} else {
		log.Printf("Received page: %s - %s", page.Title, page.Content)
	}

	// Call SetPage to set a page
	setPageStream, err := client.SetPage(context.Background())
	if err != nil {
		log.Fatalf("Failed to set page: %v", err)
	}
	if err := setPageStream.Send(&Page{
		Title:   "New Page",
		Content: "New Page Content",
		Key:     "456",
	}); err != nil {
		log.Fatalf("Failed to send page: %v", err)
	}
	setPageKey, err := setPageStream.Recv()
	if err == io.EOF {
		log.Println("No page key received")
	} else if err != nil {
		log.Fatalf("Failed to receive page key: %v", err)
	} else {
		log.Printf("Received page key: %s", setPageKey.Key)
	}
}
