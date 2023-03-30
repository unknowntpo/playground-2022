package main

import (
	"context"
	"io"
	"log"

	page "github.com/unknowntpo/playground-2022/go/grpc/page/page"

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
	if err := getPageStream.Send(&page.PageKey{Key: "123"}); err != nil {
		log.Fatalf("Failed to send page key: %v", err)
	}
	pg, err := getPageStream.Recv()
	if err == io.EOF {
		log.Println("No page received")
	} else if err != nil {
		log.Fatalf("Failed to receive page: %v", err)
	} else {
		log.Printf("Received page: %s - %s", pg.Title, pg.Content)
	}

	// Call SetPage to set a page
	setPageStream, err := client.SetPage(context.Background())
	if err != nil {
		log.Fatalf("Failed to set page: %v", err)
	}

	newPage := page.Page{
		Title:   "New Page",
		Content: "New Page Content",
	}

	if err := setPageStream.Send(&newPage); err != nil {
		log.Fatalf("Failed to send page: %v", err)
	}
	setPageKey, err := setPageStream.CloseAndRecv()
	if err == io.EOF {
		log.Println("No page key received")
	} else if err != nil {
		log.Fatalf("Failed to receive page key: %v", err)
	} else {
		log.Printf("Received page key: %s", setPageKey.Key)
	}
}
