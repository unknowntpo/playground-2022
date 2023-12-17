package main

import (
	"context"
	"log"
	"time"
)

type OrderService struct {
	orderRespChan    chan OrderResp
	orderCreatedChan chan Order
	paymentReqChan   chan PaymentReq
	paymentRespChan  chan PaymentResp
}

func NewOrderService(orderReplyChan chan OrderResp, paymentReqChan chan PaymentReq, paymentRespChan chan PaymentResp) *OrderService {
	s := &OrderService{
		orderRespChan:    orderReplyChan,
		orderCreatedChan: make(chan Order, 10),
		paymentReqChan:   paymentReqChan,
		paymentRespChan:  paymentRespChan,
	}
	return s
}

func (s *OrderService) Serve(ctx context.Context) {
	go s.onOrderCreated(ctx)
	go s.onPayment(ctx)
}

func (s *OrderService) onOrderCreated(ctx context.Context) {
	// ORDER_CREATED
	for {
		select {
		case o := <-s.orderCreatedChan:
			req := PaymentReq{
				OrderID: o.ID,
				UserID:  o.UserID,
			}
			s.paymentReqChan <- req
		case <-ctx.Done():
			return
			// Make this goroutine spin to prevent deadlock
		default:
		}
	}
}

// handle payment result from channel
func (s *OrderService) onPayment(ctx context.Context) {
	for {
		select {
		case resp := <-s.paymentRespChan:
			switch resp.State {
			case PAYMENT_SUCCEDED:
				log.Printf("[ORDER]: OrderID: [%v] Payment Succeed\n", resp.OrderID)
			case PAYMENT_FAILED:
				log.Printf("[ORDER]: OrderID: [%v] Payment FAILED\n", resp.OrderID)
			}
			s.orderRespChan <- OrderResp{Order: Order{ID: resp.OrderID, UserID: resp.UserID}, PaymentState: resp.State}
		case <-ctx.Done():
			return
		}
	}
}

func (s *OrderService) CreateOrder(req OrderReq) error {
	s.orderCreatedChan <- Order{Item: req.Item, UserID: req.UserID, State: ORDER_CREATED}
	log.Printf("[ORDER] Order created for userID: [%v], item: [%v]\n", req.UserID, req.Item)
	return nil
}

type Order struct {
	ID         int
	Item       string
	UserID     int
	Created_At time.Time
	State      OrderState
}

type OrderReq struct {
	Item       string
	UserID     int
	Created_At time.Time
}

type OrderResp struct {
	Order
	PaymentState PaymentState
}

type OrderState int

const (
	ORDER_CREATED OrderState = iota
	ORDER_PAYMENT_SUCCEDDED
	ORDER_PAYMENT_FAILED
	ORDER_SUCCEDED
)

// [ORDER] order created, id: 1, userid: 2
// [PAYMENT] charged $100 for orderID: 1
