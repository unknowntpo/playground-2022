include common.mk

## kafka/up: run kafka
kafka/up:
	docker-compose up -d

## kafka/down: kill kafka
kafka/down:
	docker-compose down -v

## kafka/topic/new/$1: create new topic named $1
kafka/topic/new/%:
	echo '$*'
	docker compose exec broker \
  kafka-topics --create \
    --topic $* \
    --bootstrap-server localhost:9092 \
    --replication-factor 1 \
    --partitions 1

## kafka/topic/list: list topics
kafka/topic/list:
	docker compose exec broker \
  kafka-topics --list \
    --bootstrap-server localhost:9092



## build/producer: build producer binary
build/producer:
	go build -o out/producer util.go producer.go

## build/consumer: build consumer binary
build/consumer:
	go build -o out/consumer util.go consumer.go

## run/producer: run producer
run/producer: build/producer
	./out/producer getting-started.properties


## run/consumer: run consumer
run/consumer: build/consumer
	./out/consumer getting-started.properties


