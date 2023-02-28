package main

import (
    "fmt"
    "github.com/go-redis/redis"
)

func main() {
    // create a new Redis client
    client := redis.NewClient(&redis.Options{
        Addr:     "localhost:6379",
        Password: "", // no password set
        DB:       0,  // use default DB
    })

    // set multiple keys and values at once
    err := client.MSet("key1", "value1", "key2", "value2", "key3", "value3").Err()
    if err != nil {
        panic(err)
    }

    // get the values for the keys we just set
    value1, err := client.Get("key1").Result()
    if err != nil {
        panic(err)
    }
    value2, err := client.Get("key2").Result()
    if err != nil {
        panic(err)
    }
    value3, err := client.Get("key3").Result()
    if err != nil {
        panic(err)
    }

    fmt.Printf("Value for key1: %s\n", value1)
    fmt.Printf("Value for key2: %s\n", value2)
    fmt.Printf("Value for key3: %s\n", value3)
}
