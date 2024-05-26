import {
  NatsConnection, RetentionPolicy, AckPolicy, StringCodec, Subscription, connect
} from "nats";
const server = { servers: "localhost:4222" };

async function pubSub() {
  const nc = await connect(server);
  console.log(`connected to ${nc.getServer()}`);


  // Create three subscribers
  const sub1 = createSubscriber(nc, 1);
  const sub2 = createSubscriber(nc, 2);
  const sub3 = createSubscriber(nc, 3);
  // Publish messages
  publishMessages(nc);

  // Handle graceful shutdown
  const shutdown = async () => {
    console.log("Shutting down...");

    // Close the subscribers
    sub1.unsubscribe();
    sub2.unsubscribe();
    sub3.unsubscribe();

    // Close the connection
    await nc.close();
    console.log(`Connection closed`);

    // Check if the close was OK
    const err = await nc.closed();
    if (err) {
      console.log(`Error closing:`, err);
    }

    process.exit();
  };

  process.on("SIGINT", shutdown);
  process.on("SIGTERM", shutdown);
}

const sc = StringCodec();

// Create a publisher
function publishMessages(nc: NatsConnection) {
  const messages = ["Hello, World!", "NATS is awesome!", "Pub-Sub example"];
  let messageIndex = 0;

  // Publish a message every second
  setInterval(() => {
    const msg = messages[messageIndex];
    nc.publish("test.subject", sc.encode(msg));
    console.log(`Publisher: Published message ${messageIndex + 1}: ${msg}`);
    messageIndex = (messageIndex + 1) % messages.length;
  }, 1000);
}

// Create a subscriber
function createSubscriber(nc: NatsConnection, id: number): Subscription {
  const sub = nc.subscribe("test.subject");
  (async () => {
    for await (const msg of sub) {
      console.log(`Subscriber ${id}: Received message: ${sc.decode(msg.data)}`);
    }
  })();
  return sub;
}

async function jetStream() {
  const nc = await connect(server);
  console.log(`connected to ${nc.getServer()}`);


  const js = nc.jetstream();

  const jsm = await js.jetstreamManager();
  await jsm.streams.add({
    name: "EVENTS",
    retention: RetentionPolicy.Workqueue,
    subjects: ["events.>"],
  })
  console.log(`created the stream`)

  await Promise.all([
    js.publish("events.us.page_loaded"),
    js.publish("events.us.mouse_clicked"),
    js.publish("events.us.input_focused"),
  ])

  console.log("# Stream info without consumers");
  console.log((await jsm.streams.info("EVENTS")).state);


  await jsm.consumers.add("EVENTS", {
    durable_name: "worker",
    ack_policy: AckPolicy.Explicit,
  });

  const c = await js.consumers.get("EVENTS", "worker");

  const iter = await c.fetch({ max_messages: 3 });
  for await (const m of iter) {
    console.log(`got: ${m.subject}`);
    m.ack();
  }

  console.log("# Stream info with one consumer");
  console.log((await jsm.streams.info("EVENTS")).state);



  // Handle graceful shutdown
  const shutdown = async () => {
    console.log("Shutting down...");

    // Close the connection
    await nc.close();
    console.log(`Connection closed`);

    // Check if the close was OK
    const err = await nc.closed();
    if (err) {
      console.log(`Error closing:`, err);
    }

    process.exit();
  };

  process.on("SIGINT", shutdown);
  process.on("SIGTERM", shutdown);
}

jetStream()

// pubSub()
