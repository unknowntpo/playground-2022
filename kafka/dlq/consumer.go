package main

import (
	"fmt"
	"math/rand"
	"os"
	"os/signal"
	"strconv"
	"syscall"
	"time"

	"github.com/confluentinc/confluent-kafka-go/kafka"
)

const ORDER_TOPIC = "order"
const RETRY_LIMIT = 2
const ORDER_DLQ_TOPIC = "order_dlq"

func main() {

	if len(os.Args) != 2 {
		fmt.Fprintf(os.Stderr, "Usage: %s <config-file-path>\n",
			os.Args[0])
		os.Exit(1)
	}

	rand.Seed(time.Now().UnixNano())

	configFile := os.Args[1]
	conf := ReadConfig(configFile)
	conf["group.id"] = "kafka-go-getting-started"
	conf["auto.offset.reset"] = "earliest"

	p, err := kafka.NewProducer(&conf)
	if err != nil {
		fmt.Printf("Failed to create producer: %s", err)
		os.Exit(1)
	}

	c, err := kafka.NewConsumer(&conf)

	if err != nil {
		fmt.Printf("Failed to create consumer: %s", err)
		os.Exit(1)
	}

	topics := []string{ORDER_TOPIC}
	topics = append(topics, getRetryTopicNames(RETRY_LIMIT)...)
	topics = append(topics, ORDER_DLQ_TOPIC)
	err = c.SubscribeTopics(topics, nil)
	// Set up a channel for handling Ctrl-C, etc
	sigchan := make(chan os.Signal, 1)
	signal.Notify(sigchan, syscall.SIGINT, syscall.SIGTERM)

	// Process messages
	run := true
	for run {
		select {
		case sig := <-sigchan:
			fmt.Printf("Caught signal %v: terminating\n", sig)
			run = false
		default:
			ev, err := c.ReadMessage(100 * time.Millisecond)
			if err != nil {
				// Errors are informational and automatically handled by the consumer
				fmt.Printf("err: %v\n", err)
				continue
			}

			var order Order
			Unmarshal(ev.Value, &order)

			fmt.Printf("Consumed event from topic %s, offset %s, key = %-10s value = %+v\n",
				*ev.TopicPartition.Topic, ev.TopicPartition.Offset.String(), string(ev.Key), order)

			randNum := rand.Intn(10)
			if randNum < 3 {
				// Success
				fmt.Printf("SUCCESS\n")
			} else {
				fmt.Printf("FAIL\n")
				// goto retry queue
				if order.Retry < RETRY_LIMIT {
					order.Retry++
					retryTopic := getRetryTopicName(order.Retry)
					p.Produce(&kafka.Message{
						TopicPartition: kafka.TopicPartition{
							Topic:     &retryTopic,
							Partition: kafka.PartitionAny,
						},
						Key:   []byte(order.Name),
						Value: Marshal(order),
					}, nil)

					fmt.Printf("Published message %+v to %v\n", debug(order), retryTopic)
				} else {
					// dlq
					order.Retry++

					fmt.Printf("Publish message %+v to dlq\n", debug(order))
					dlqTopic := ORDER_DLQ_TOPIC
					must(p.Produce(&kafka.Message{
						TopicPartition: kafka.TopicPartition{
							Topic:     &dlqTopic,
							Partition: kafka.PartitionAny,
						},
						Key:   []byte(order.Name),
						Value: Marshal(order),
					}, nil))
				}
			}
		}
	}

	c.Close()
}

func getRetryTopicName(retry int) string {
	return fmt.Sprintf("order_retry_%s", strconv.Itoa(retry))
}

func getRetryTopicNames(retry int) []string {
	out := make([]string, 0, retry)
	for i := 1; i <= retry; i++ {
		out = append(out, getRetryTopicName(i))
	}
	return out
}
