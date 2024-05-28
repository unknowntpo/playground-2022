import {
  NatsConnection, RetentionPolicy, AckPolicy, StringCodec, Subscription, connect
} from "nats";
const server = { servers: "localhost:4222" };

async function startSubscriber(id: number) {
  const nc = await connect(server);
  console.log(`connected to ${nc.getServer()}`);

  const js = nc.jetstream();

  const jsm = await js.jetstreamManager();

  console.log("# Stream info without consumers");
  console.log((await jsm.streams.info("JOBQUEUES")).state);


  const workerName = `worker-${id}`;
  await jsm.consumers.add("JOBQUEUES", {
    durable_name: workerName,
    ack_policy: AckPolicy.Explicit,
    filter_subject: 'jobQueues.crawlers.>'
  });

  const c = await js.consumers.get("JOBQUEUES", workerName);

  const iter = await c.fetch({ max_messages: 3 });
  for await (const m of iter) {
    console.log(`got msg: ${m.subject}, ${m.string()}`);
    m.ack();
  }

  console.log("# Stream info with one consumer");
  console.log((await jsm.streams.info("JOBQUEUES")).state);



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

export { startSubscriber };
