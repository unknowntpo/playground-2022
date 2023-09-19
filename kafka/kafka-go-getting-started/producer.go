package main

import (
	"fmt"
	"math/rand"
	"os"
	"strings"
	"time"

	"github.com/IBM/sarama"
)

func main() {
	brokers := strings.Split(os.Getenv("KAFKA_BROKERS"), ",")

	config := sarama.NewConfig()
	config.Producer.Return.Successes = true
	config.Version = sarama.V0_10_2_0

	var producer sarama.SyncProducer
	retry := 10
	for retry != 0 {
		var err error
		producer, err = sarama.NewSyncProducer(brokers, config)
		if err == nil {
			break
		}
		if retry > 0 {
			time.Sleep(1 * time.Second)
			retry--
			fmt.Printf("Error creating Kafka producer, retrying ...: %v\n", err)
		} else {
			fmt.Printf("Error creating Kafka producer: %v\n", err)
			return
		}
	}

	defer producer.Close()

	fmt.Println("producer is now established")

	topic := "purchases"

	users := [...]string{"eabara", "jsmith", "sgarcia", "jbernard", "htanaka", "awalther"}
	items := [...]string{"book", "alarm clock", "t-shirts", "gift card", "batteries"}

	for n := 0; n < 10; n++ {
		key := users[rand.Intn(len(users))]
		data := items[rand.Intn(len(items))]

		message := &sarama.ProducerMessage{
			Topic: topic,
			Key:   sarama.StringEncoder(key),
			Value: sarama.StringEncoder(data),
		}

		partition, offset, err := producer.SendMessage(message)
		if err != nil {
			fmt.Printf("Failed to produce message: %v\n", err)
		} else {
			fmt.Printf("Produced event to topic %s: key = %-10s value = %s, partition = %d, offset = %d\n",
				topic, key, data, partition, offset)
		}

		// Sleep for a short duration to simulate message production delay
		time.Sleep(100 * time.Millisecond)
	}
}
