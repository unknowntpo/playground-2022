package main

import (
	"context"
	"fmt"
	"os"
	"os/signal"
	"strings"
	"syscall"
	"time"

	"github.com/Shopify/sarama"
)

func main() {
	brokers := strings.Split(os.Getenv("KAFKA_BROKERS"), ",")
	groupID := "kafka-go-getting-started"

	config := sarama.NewConfig()
	config.Version = sarama.V0_10_2_0
	config.Consumer.Group.Rebalance.Strategy = sarama.BalanceStrategyRoundRobin
	config.Consumer.Return.Errors = true

	config.Consumer.Offsets.Initial = sarama.OffsetOldest

	client, err := sarama.NewClient(brokers, config)
	if err != nil {
		panic(err)
	}

	fmt.Println("brokers: ", client.Brokers())

	var consumer sarama.ConsumerGroup
	retry := 10
	for retry != 0 {
		var err error
		consumer, err = sarama.NewConsumerGroupFromClient(groupID, client)
		if err == nil {
			break
		}
		if retry > 0 {
			time.Sleep(1 * time.Second)
			retry--
			fmt.Printf("Error creating Kafka consumer, retrying ...: %v\n", err)
		} else {
			fmt.Printf("Error creating Kafka consumer: %v\n", err)
			return
		}
	}

	defer consumer.Close()

	// track errors
	go func() {
		for err := range consumer.Errors() {
			fmt.Println("ERROR", err)
		}
	}()

	topic := "purchases"

	// Set up a channel for handling Ctrl-C, etc
	sigchan := make(chan os.Signal, 1)
	signal.Notify(sigchan, syscall.SIGINT, syscall.SIGTERM)

	// Define a handler for consuming messages
	handler := &ConsumerHandler{}

	// Consume messages
	for {
		select {
		case sig := <-sigchan:
			fmt.Printf("Caught signal %v: terminating\n", sig)
			return
		default:
			if err := consumer.Consume(context.Background(), []string{topic}, handler); err != nil {
				fmt.Printf("Error consuming message: %v\n", err)
			}
		}
	}
}

// ConsumerHandler implements sarama.ConsumerGroupHandler
type ConsumerHandler struct{}

func (h *ConsumerHandler) Setup(session sarama.ConsumerGroupSession) error   { return nil }
func (h *ConsumerHandler) Cleanup(session sarama.ConsumerGroupSession) error { return nil }

func (h *ConsumerHandler) ConsumeClaim(session sarama.ConsumerGroupSession, claim sarama.ConsumerGroupClaim) error {
	for message := range claim.Messages() {
		fmt.Printf("Consumed event from topic %s: key = %-10s value = %s\n",
			message.Topic, string(message.Key), string(message.Value))
		session.MarkMessage(message, "")
	}
	return nil
}
