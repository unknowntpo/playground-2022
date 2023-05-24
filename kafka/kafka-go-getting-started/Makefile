include common.mk

## kafka/topic/new/$1: create new topic named $1
kafka/topic/new/%:
	echo '$*'
	docker compose exec broker \
  kafka-topics --create \
    --topic $* \
    --bootstrap-server localhost:9092 \
    --replication-factor 1 \
    --partitions 1
