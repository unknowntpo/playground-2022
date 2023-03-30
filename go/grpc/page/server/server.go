package main

import (
	"io"
	"log"

	"github.com/unknowntpo/playground-2022/go/grpc/page/page"
)

type pageService struct{}

func (s *pageService) GetHead(req *page.GetHeadRequest, stream page.PageService_GetHeadServer) error {
	// TODO: Implement the logic to get the head page key
	// For example:
	pageKey := "123"

	// Send the page key through the stream
	if err := stream.Send(&page.PageKey{Key: pageKey}); err != nil {
		return err
	}

	return nil
}

func (s *pageService) GetPage(stream page.PageService_GetPageServer) error {
	// Receive the page keys from the stream and send the corresponding pages
	for {
		pageKey, err := stream.Recv()
		if err == io.EOF {
			return nil
		}
		if err != nil {
			return err
		}

		_ = pageKey

		// TODO: Implement the logic to get the page
		// For example:
		page := &page.Page{
			Title:   "Page Title",
			Content: "Page Content",
		}

		// Send the page through the stream
		if err := stream.Send(page); err != nil {
			return err
		}
	}
}

func (s *pageService) SetPage(stream page.PageService_SetPageServer) error {
	// Receive the pages from the stream and set them
	for {
		pg, err := stream.Recv()
		if err == io.EOF {
			return nil
		}
		if err != nil {
			return err
		}

		// TODO: Implement the logic to set the page
		// For example:
		pageKey := "ABC"
		log.Printf("Setting page %s: %s", pageKey, pg.Content)

		// Send the page key through the stream
		if err := stream.SendAndClose(&page.PageKey{Key: pageKey}); err != nil {
			return err
		}
	}
}
