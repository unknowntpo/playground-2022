package main

import (
	"context"
	"log"
	"math/rand"
	"time"
)

type Payment struct {
	OrderID int
}

type PaymentReq struct {
	OrderID int
	UserID  int
}

type PaymentResp struct {
	OrderID int
	UserID  int
	State   PaymentState
}

type PaymentService struct {
	paymentReqChan  chan PaymentReq
	paymentRespChan chan PaymentResp
}

type PaymentState int

const (
	PAYMENT_CREATED PaymentState = iota
	PAYMENT_SUCCEDED
	PAYMENT_FAILED
)

func NewPaymentService(paymentReqChan chan PaymentReq, paymentRespChan chan PaymentResp) *PaymentService {
	return &PaymentService{
		paymentReqChan:  paymentReqChan,
		paymentRespChan: paymentRespChan,
	}
}

func (s *PaymentService) Serve(ctx context.Context) {
	go s.handlePayments(ctx)
}

func (s *PaymentService) handlePayments(ctx context.Context) {
	for {
		select {
		case req := <-s.paymentReqChan:
			// Simulate payment operation
			s.processPayment(req)
		case <-ctx.Done():
			return
		}
	}
}

func (s *PaymentService) processPayment(req PaymentReq) {
	rand.Seed(time.Now().UnixNano())
	paymentState := PAYMENT_FAILED // default to failed

	if rand.Intn(2) == 1 { // 50% chance of success
		paymentState = PAYMENT_SUCCEDED
	}

	resp := PaymentResp{
		OrderID: req.OrderID,
		UserID:  req.UserID,
		State:   paymentState,
	}
	s.paymentRespChan <- resp
	log.Printf("[PAYMENT] Processed payment for orderID: [%v], State: [%v]\n", req.OrderID, paymentState)
}

// Other types and constants remain the same as in your original code.
