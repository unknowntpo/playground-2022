package main

import (
	"context"
	"fmt"
	"math/rand"
	"sync"
	"time"

	"github.com/go-redis/redis/v9"
)

type Data struct {
	Name string `json:"name"`
	Age  int    `json:"age"`
}

const SortedSetKey = "zset"

func main() {
	client := redis.NewClient(&redis.Options{
		Addr:     "localhost:6379",
		Password: "",
		DB:       0,
	})

	client.ZAdd(context.Background(), SortedSetKey, redis.Z{Score: 0.0, Member: "initMember"})

	// Create a wait group to synchronize the workers
	wg := &sync.WaitGroup{}

	// Start four workers
	for i := 0; i < 4; i++ {
		wg.Add(1)
		go worker(client, wg)
	}

	// Wait for all workers to complete
	wg.Wait()

	// Retrieve the members of the sorted set
	members, err := client.ZRangeWithScores(context.Background(), SortedSetKey, 0, -1).Result()
	if err != nil {
		panic(err)
	}
	for _, member := range members {
		fmt.Printf("Member: %v, Score: %f\n", member.Member, member.Score)
	}
}

func worker(client *redis.Client, wg *sync.WaitGroup) {
	defer wg.Done()

	// Create a Lua script to get the max score and add a new value
	script := redis.NewScript(`
		redis.log(redis.LOG_NOTICE, "gotValue", ARGV[1])

		local maxScore = redis.call("ZREVRANGE", KEYS[1], 0, 0, "WITHSCORES")
		local newScore = tonumber(maxScore[2]) + 1
		local result = redis.call("ZADD", KEYS[1], newScore, ARGV[1])
		if result ~= 1 then
			return {err = "ZADD failed"}
		end

		redis.log(redis.LOG_NOTICE, "doneWithValue", ARGV[1])

		return newScore
	`)

	// Use WATCH/MULTI/EXEC to ensure that the script is run in a transaction
	value := fmt.Sprintf("new-value-%s", GenerateRandomString(3))
	err := client.Watch(context.Background(), func(tx *redis.Tx) error {
		// Get the max score and add the new value
		result, err := script.Run(context.Background(), tx, []string{SortedSetKey}, value).Result()
		if err != nil {
			return err
		}

		// Print the result
		fmt.Printf("Added value %q with score %v\n", value, result)

		return nil
	}, SortedSetKey)

	if err != nil {
		fmt.Println("Transaction failed:", err)
	}
}

const letterBytes = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

func GenerateRandomString(length int) string {
	rand.Seed(time.Now().UnixNano())

	b := make([]byte, length)
	for i := range b {
		b[i] = letterBytes[rand.Intn(len(letterBytes))]
	}
	return string(b)
}
