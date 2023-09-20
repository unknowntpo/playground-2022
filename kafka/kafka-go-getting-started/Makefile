include common.mk

## kafka/up: run kafka
kafka/up:
	docker-compose up -d

## kafka/down: kill kafka
kafka/down:
	docker-compose down -v

## kafka/helm/install: install kafka helm chart
kafka/helm/install:
	helm repo add strimzi https://strimzi.io/charts/
	helm install kafka strimzi/strimzi-kafka-operator

## kafka/resources/install: install kafka svc
kafka/resources/install:
	kc apply -f ./k8s/kafka-resources.yml

## kafka/topic/new/$1: create new topic named $1
kafka/topic/new/%:
	echo '$*'
	docker compose exec kafka1 \
  kafka-topics --create \
    --topic $* \
    --bootstrap-server localhost:9092 \
    --replication-factor 1 \
    --partitions 1

CGO=CGO_ENABLED=0
# https://github.com/confluentinc/confluent-kafka-go/issues/898#issuecomment-1313054432
## build/producer: build producer binary
build/producer:
	$(CGO) go build -o out/producer producer.go

## build/consumer: build consumer binary
build/consumer:
	$(CGO) go build -o out/consumer consumer.go

## run/producer: run producer
run/producer: build/producer
	./out/producer getting-started.properties

PLATFORM=--platform arm
IMG_TAG= -t $(IMAGE)
## image/build/producer: build producer image
image/build/producer:
	DOCKER_BUILDKIT=1 docker build $(PLATFORM) $(IMG_TAG) --target producer .

## image/build/consumer: build consumer image
image/build/consumer:
	DOCKER_BUILDKIT=1 docker build $(PLATFORM) $(IMG_TAG) --target consumer .

## run/consumer: run consumer
run/consumer: build/consumer
	./out/consumer

## skaffold: run skaffold
skaffold:
	echo 'Running skaffold dev...'
	@skaffold dev
