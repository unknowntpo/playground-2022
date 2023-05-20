package main

import (
	"context"
	"fmt"
	"log"
	"time"

	"github.com/segmentio/kafka-go"
)

const (
	topic          = "test-topic"
	broker1Address = "localhost:9092"
	broker2Address = "localhost:9093"
)

func main() {
	// to create topics when auto.create.topics.enable='true'
	// Ref: https://github.com/segmentio/kafka-go#to-create-topics
	_, err := kafka.DialLeader(context.Background(), "tcp", broker1Address, topic, 0)
	if err != nil {
		panic(err.Error())
	}
	// Start producer goroutine
	go producer()

	// Start two consumer goroutines
	go consumer(1)
	go consumer(2)

	// Sleep for a while to allow the consumers and producer to finish
	time.Sleep(5 * time.Second)
}

func producer() {
	// Create a new Kafka writer
	writer := kafka.NewWriter(kafka.WriterConfig{
		Brokers: []string{broker1Address, broker2Address},
		Topic:   topic,
	})

	defer writer.Close()

	// Produce messages
	for i := 0; i < 5; i++ {
		message := fmt.Sprintf("Message %d", i)
		err := writer.WriteMessages(context.Background(), kafka.Message{
			Key:   []byte(fmt.Sprintf("Key %d", i)),
			Value: []byte(message),
		})

		if err != nil {
			log.Printf("Failed to write message: %v", err)
		} else {
			log.Printf("Produced message: %s", message)
		}

		time.Sleep(1 * time.Second)
	}
}

func consumer(id int) {
	// Create a new Kafka reader
	reader := kafka.NewReader(kafka.ReaderConfig{
		Brokers: []string{broker1Address, broker2Address},
		Topic:   topic,
		GroupID: fmt.Sprintf("consumer-group-%d", id),
	})

	defer reader.Close()

	// Consume messages
	for {
		msg, err := reader.ReadMessage(context.Background())
		if err != nil {
			log.Printf("Failed to read message: %v", err)
			break
		}

		log.Printf("Consumer %d received message: %s", id, string(msg.Value))
	}
}
