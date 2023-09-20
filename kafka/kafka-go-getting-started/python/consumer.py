from confluent_kafka import Consumer, KafkaError, KafkaException
import os
import signal
import sys
import time


def main():
    brokers = os.getenv("KAFKA_BROKERS").split(',')
    group_id = "kafka-python-getting-started"
    topic = "purchases"

    conf = {
        'bootstrap.servers': ','.join(brokers),
        'group.id': group_id,
        'auto.offset.reset': 'earliest'
    }

    consumer = Consumer(conf)

    def shutdown(signal, frame):
        sys.stderr.write('Aborted by user\n')
        consumer.close()
        sys.exit(1)

    signal.signal(signal.SIGINT, shutdown)
    signal.signal(signal.SIGTERM, shutdown)

    retry = 10
    while retry > 0:
        try:
            consumer.subscribe([topic])
            break
        except KafkaException as e:
            if retry > 0:
                time.sleep(1)
                retry -= 1
                print(f"Error creating Kafka consumer, retrying ...: {e}")
            else:
                print(f"Error creating Kafka consumer: {e}")
                return

    while True:
        try:
            msg = consumer.poll(1.0)

            if msg is None:
                continue
            if msg.error():
                if msg.error().code() == KafkaError._PARTITION_EOF:
                    continue
                else:
                    print(f"Error while polling for messages: {msg.error()}")
            else:
                print(
                    f"Consumed event from topic {msg.topic()}, partition {msg.partition()}: key = {msg.key()} value = {msg.value()}")

        except KeyboardInterrupt:
            break

    consumer.close()


if __name__ == "__main__":
    main()
