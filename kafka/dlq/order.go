package main

import (
	"math/rand"
)

type Order struct {
	Name  string `json:"name"`
	Data  string `json:"data"`
	Retry int    `json:"retry"`
}

func CreateOrderData() Order {
	users := [...]string{"eabara", "jsmith", "sgarcia", "jbernard", "htanaka", "awalther"}
	items := [...]string{"book", "alarm clock", "t-shirts", "gift card", "batteries"}
	key := users[rand.Intn(len(users))]
	data := items[rand.Intn(len(items))]
	return Order{Name: key, Data: data, Retry: 0}
}
