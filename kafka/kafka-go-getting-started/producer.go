package main

import (
	"context"
	"fmt"
	"math/rand"
	"os"
	"strings"
	"time"

	kafka "github.com/segmentio/kafka-go"
)

func main() {
	brokers := strings.Split(os.Getenv("KAFKA_BROKERS"), ",")

	topic := "purchases"
	writer := kafka.NewWriter(kafka.WriterConfig{
		Brokers:  brokers,
		Topic:    topic,
		Balancer: &kafka.LeastBytes{},
	})

	defer writer.Close()

	users := [...]string{"eabara", "jsmith", "sgarcia", "jbernard", "htanaka", "awalther"}
	items := [...]string{"book", "alarm clock", "t-shirts", "gift card", "batteries"}

	for n := 0; n < 10; n++ {
		key := users[rand.Intn(len(users))]
		data := items[rand.Intn(len(items))]

		msg := kafka.Message{
			Key:   []byte(key),
			Value: []byte(data),
		}

		err := writer.WriteMessages(context.Background(), msg)
		if err != nil {
			fmt.Printf("Failed to produce message: %s\n", err)
		} else {
			fmt.Printf("Produced event to topic %s: key = %-10s value = %s\n",
				topic, string(msg.Key), string(msg.Value))
		}

		// Sleep for a short duration to simulate message production delay
		time.Sleep(100 * time.Millisecond)
	}
}

// Define KafkaConfig as per your configuration structure.
type KafkaConfig struct {
	Brokers []string
	// Add other configuration parameters as needed
}
