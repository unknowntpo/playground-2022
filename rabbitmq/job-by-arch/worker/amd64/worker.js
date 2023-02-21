const amqp = require('amqplib');

var channel, connection;

var channel, connection;  //global variables
async function connectQueue() {
    try {
        connection = await amqp.connect("amqp://127.0.0.1:5672");
        channel = await connection.createChannel()

        await channel.assertQueue("test-queue")

    } catch (error) {
        console.log(error)
    }
}

async function receiveFromQueue(queueName) {
    try {
        const connection = await amqp.connect("amqp://127.0.0.1:5672");

        console.log(`connection established`);

        const channel = await connection.createChannel();

        console.log(`channel created`);

        await channel.checkQueue(queueName);

        console.log(`queue exist`);

        channel.consume(queueName, (message) => {
            console.log(`Receive message: ${message.content.toString()}`);
            channel.ack(message);
        });
    } catch (error) {
        console.error(`Error ${error}`);
    }
}

const delay = ms => new Promise(resolve => setTimeout(resolve, ms))

function main() {
    // connectQueue()
    // while (true) {
    receiveFromQueue("amd64")
    // delay(1000)
    // }
}


main()
