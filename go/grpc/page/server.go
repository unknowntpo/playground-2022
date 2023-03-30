package main

import (
	"io"
	"log"
)

type pageService struct{}

func (s *pageService) GetHead(req *GetHeadRequest, stream PageService_GetHeadServer) error {
	// TODO: Implement the logic to get the head page key
	// For example:
	pageKey := "123"

	// Send the page key through the stream
	if err := stream.Send(&PageKey{Key: pageKey}); err != nil {
		return err
	}

	return nil
}

func (s *pageService) GetPage(stream PageService_GetPageServer) error {
	// Receive the page keys from the stream and send the corresponding pages
	for {
		pageKey, err := stream.Recv()
		if err == io.EOF {
			return nil
		}
		if err != nil {
			return err
		}

		// TODO: Implement the logic to get the page
		// For example:
		page := &Page{
			Title:   "Page Title",
			Content: "Page Content",
			Key:     pageKey.Key,
		}

		// Send the page through the stream
		if err := stream.Send(page); err != nil {
			return err
		}
	}
}

func (s *pageService) SetPage(stream PageService_SetPageServer) error {
	// Receive the pages from the stream and set them
	for {
		page, err := stream.Recv()
		if err == io.EOF {
			return nil
		}
		if err != nil {
			return err
		}

		// TODO: Implement the logic to set the page
		// For example:
		log.Printf("Setting page %s: %s", page.Key, page.Content)

		// Send the page key through the stream
		if err := stream.Send(&PageKey{Key: page.Key}); err != nil {
			return err
		}
	}
}
