import {
  NatsConnection, RetentionPolicy, AckPolicy, StringCodec, Subscription, connect,
  StorageType
} from "nats";
const server = { servers: "localhost:4222" };

const sc = StringCodec();

async function startPublisher() {
  const nc = await connect(server);

  console.log(`startPublisher...`);
  console.log(`connected to ${nc.getServer()}`);


  const js = nc.jetstream();

  const jsm = await js.jetstreamManager();
  await jsm.streams.add({
    name: "JOBQUEUES",
    storage: StorageType.File,
    retention: RetentionPolicy.Workqueue,
    subjects: ["jobQueues.>"],
  })
  console.log(`created the stream`)

  await Promise.all([
    js.publish("jobQueues.crawlers.A", `{"url": "https://google.com"}`),
    js.publish("jobQueues.crawlers.B", `{"url": "https://example.com"}`),
  ])

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

export { startPublisher };
