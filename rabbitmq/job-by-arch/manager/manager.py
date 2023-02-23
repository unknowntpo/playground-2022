import pika
import sys
import os

exc_name = 'route_job'


class PikaPublisher:
    def __init__(self, host, port, exchange_name):
        self.exchange_name = exchange_name
        self.connection = pika.BlockingConnection(
            pika.ConnectionParameters(host=host, port=port)
        )
        self.channel = self.connection.channel()
        self.channel.exchange_declare(
            exchange=exchange_name, exchange_type='direct')

    def publish(self, message, routing_key):
        self.channel.basic_publish(
            exchange=self.exchange_name,
            routing_key=routing_key,
            body=message
        )

    def close(self):
        self.connection.close()


class Config:
    def __init__(self, host, port, exchange_name):
        self.host = host
        self.port = port
        self.exchange_name = exchange_name

    # def get_host(self):
    #     return self.host


def environ():
    rabbitMQ_host = os.environ.get("RABBITMQ_HOST", "localhost")
    rabbitMQ_port = int(os.environ.get("RABBITMQ_PORT", 5672))
    return Config(host=rabbitMQ_host, port=rabbitMQ_port, exchange_name=exc_name)


def main():
    cfg = environ()
    publisher = PikaPublisher(
        host=cfg.host, port=cfg.port, exchange_name=exc_name)
    publisher.publish('fib(30)', 'aarch64')
    publisher.close()


if __name__ == "__main__":
    main()
