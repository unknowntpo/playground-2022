package main

import (
	"context"
	"fmt"
	"log"
	"time"
)

func main() {
	orderReplyChan := make(chan OrderResp, 10)
	paymentReqChan := make(chan PaymentReq, 10)
	paymentRespChan := make(chan PaymentResp, 10)
	orderSvc := NewOrderService(orderReplyChan, paymentReqChan, paymentRespChan)
	ctx := context.Background()
	go orderSvc.Serve(ctx)

	paymentSvc := NewPaymentService(paymentReqChan, paymentRespChan)
	go paymentSvc.Serve(ctx)

	// create fake order

	for i := 1; i <= 10; i++ {
		orderReq := OrderReq{
			Item:       fmt.Sprintf("Item %d", i),
			UserID:     i,
			Created_At: time.Now(),
		}

		err := orderSvc.CreateOrder(orderReq)
		if err != nil {
			log.Printf("Error creating order: %v\n", err)
		}
	}

	for resp := range orderReplyChan {
		fmt.Printf("[CLIENT]: Resp of order: %v\n", resp)
	}
}
