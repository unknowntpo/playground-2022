const amqp = require('amqplib')

const url = 'amqp://127.0.0.1'

const queue = 'my_queue';

async function produce(producerName) {
  for (i = 0; i < 10; i++) {
    try {
      const connection = await amqp.connect(url);
      const channel = await connection.createChannel()

      const message = `Hello, RabbitMQ ${i}`

      await channel.assertQueue(queue)
      await channel.sendToQueue(queue, Buffer.from(message))

      console.log(`${producerName}: message sent:`, message)

      await channel.close()
      await connection.close()
    } catch (error) {
      console.error(`${producerName} error:`, error)
    }
    await sleep(1000)
  }
}

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

async function consume(consumerName) {
  while (1) {
    try {
      const connection = await amqp.connect(url);
      const channel = await connection.createChannel();

      await channel.assertQueue(queue);
      await channel.consume(queue, (message) => {
        console.log(`Consummer ${consumerName} received message:`, message.content.toString());
        if (Math.random() > 0.5) {
          channel.ack(message);
        } else {
          channel.nack(message);
        }
      });
    } catch (error) {
      console.error(`${consumerName} error:`, error);
    }
    await sleep(1000)
  }
}

async function start() {
  try {
    await Promise.all([
      produce('Producer 1'),
      consume('Consummer 1'),
      consume('Consummer 1')
    ])
  } catch (error) {
    console.error('Error: ', error)
  }
}

start()

