package main

import (
	"log"
	"os"
	"os/signal"
	"syscall"

	"github.com/nsqio/go-nsq"
)

type myMessageHandler struct{}

func (h *myMessageHandler) HandleMessage(m *nsq.Message) error {
	log.Printf("這個訊息轉換成字串就是：%v", string(m.Body))
	return nil
}

func main() {
	//建立Consumer
	config := nsq.NewConfig()
	consumer, err := nsq.NewConsumer("sample_topic", "default", config)
	if err != nil {
		log.Fatal(err)
	}
	//新增一個handler來處理收到訊息時動作
	consumer.AddHandler(&myMessageHandler{})

	//連線到NSQD
	err = consumer.ConnectToNSQD("localhost:4150")
	if err != nil {
		log.Fatal(err)
	}

	//卡住，不要讓main.go執行完就結束
	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, syscall.SIGINT, syscall.SIGTERM)
	<-sigChan

	consumer.Stop()
}
