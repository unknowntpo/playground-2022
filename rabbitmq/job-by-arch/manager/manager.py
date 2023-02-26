import functools
import logging
import pika
import sys
import os
import time

exc_name = 'route_job'


def log_call(func):
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        logging.info(
            f"Calling function {func.__name__} with args {args} and kwargs {kwargs}")
        return func(*args, **kwargs)
    return wrapper


class PikaPublisher:
    def __init__(self, host, port, exchange_name):
        self.exchange_name = exchange_name
        self.connection = pika.BlockingConnection(
            pika.ConnectionParameters(host=host, port=port)
        )
        self.channel = self.connection.channel()
        self.channel.exchange_declare(
            exchange=exchange_name, exchange_type='direct')

        # declare queues
        self.queue_names = ["aarch64", "amd64"]
        self.declare_queues(self.queue_names)
        print("queue declared")

        # Bind queues
        self.bind_queues(self.queue_names)
        print("queue binded")

    @log_call
    def declare_queues(self, queue_names):
        for q_name in queue_names:
            print("declaring queue", q_name)
            result = self.channel.queue_declare(queue=q_name, exclusive=False)

    @log_call
    def bind_queues(self, queue_names):
        for q_name in queue_names:
            print("binding queue", q_name)
            route_key = q_name
            self.channel.queue_bind(exchange=self.exchange_name,
                                    queue=q_name,
                                    routing_key=route_key)

    @log_call
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

    while 1:
        publisher.publish('fib(30)', 'aarch64')
        time.sleep(1)
    publisher.close()


if __name__ == "__main__":
    main()
