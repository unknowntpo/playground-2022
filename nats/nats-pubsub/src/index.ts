import {
  NatsConnection, RetentionPolicy, AckPolicy, StringCodec, Subscription, connect
} from "nats";

import { Command } from 'commander';
import { startPublisher } from "src/publisher";
import { startSubscriber } from "src/subscriber";


async function main() {
  const program = new Command();

  program.name('vision')
    .description('publisher and subscriber')
    .version('0.0.1');

  program.option('-s, --subscriber', 'publisher or subscriber (default is publisher)')

  program.parse(process.argv);

  const options = program.opts();

  if (options.subscriber) {
    await startSubscriber(0)
  } else {
    await startPublisher()
  }
}



main()

