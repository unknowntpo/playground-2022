import pika
import sys

exc_name = 'route_job'


class PikaPublisher:
    def __init__(self, exchange_name):
        self.exchange_name = exchange_name
        self.connection = pika.BlockingConnection(
            pika.ConnectionParameters(host='localhost')
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


def main():
    publisher = PikaPublisher(exchange_name=exc_name)
    publisher.publish('fib(30)', 'aarch64')
    publisher.close()


if __name__ == "__main__":
    main()
