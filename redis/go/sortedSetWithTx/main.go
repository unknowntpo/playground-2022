package main

import (
	"context"
	"encoding/json"
	"fmt"
	"strconv"
	"sync"
	"time"

	"github.com/go-redis/redis/v9"
)

type Data struct {
	Name string `json:"name"`
	Age  int    `json:"age"`
}

func main() {
	client := redis.NewClient(&redis.Options{
		Addr:     "localhost:6379",
		Password: "",
		DB:       0,
	})

	// Create a wait group to synchronize the workers
	wg := &sync.WaitGroup{}

	// Start four workers
	for i := 0; i < 4; i++ {
		wg.Add(1)
		go worker(client, wg)
	}

	// Wait for all workers to complete
	wg.Wait()
}

func worker(client *redis.Client, wg *sync.WaitGroup) {
	defer wg.Done()

	// Watch the sorted set
	watch := func(tx *redis.Tx) error {
		_, err := tx.Pipelined(context.Background(), func(pipe redis.Pipeliner) error {
			pipe.ZRange(context.Background(), "my_sorted_set", 0, -1)
			return nil
		})
		if err != nil {
			return err
		}
		return nil
	}

	// Start a transaction with pipeline
	tx := client.TxPipeline()

	// Add hash as value and current timestamp as score to sorted set
	score := float64(time.Now().UnixNano())
	data := Data{Name: "Alice", Age: 25}
	jsonData, err := json.Marshal(data)
	if err != nil {
		panic(err)
	}
	hash := "my_hash"
	if _, err := tx.ZAdd(context.Background(), "my_sorted_set", redis.Z{Score: score, Member: hash}).Result(); err != nil {
		panic(err)
	}

	// Use SET to set hash as key and some JSON string data as value
	if _, err := tx.Set(context.Background(), hash, jsonData, 0).Result(); err != nil {
		panic(err)
	}

	// Commit the transaction
	if err := client.Watch(context.Background(), watch, "my_sorted_set"); err != nil {
		panic(err)
	}

	if _, err := tx.Exec(context.Background()); err != nil {
		panic(err)
	}

	// Retrieve the value from Redis to ensure that it was set correctly
	val, err := client.Get(context.Background(), hash).Result()
	if err != nil {
		panic(err)
	}
	fmt.Println(val)

	// Retrieve the members of the sorted set
	members, err := client.ZRange(context.Background(), "my_sorted_set", 0, -1).Result()
	if err != nil {
		panic(err)
	}
	for _, member := range members {
		score, _ := strconv.ParseFloat(member, 64)
		fmt.Printf("Member: %s, Score: %f\n", member, score)
	}
}
