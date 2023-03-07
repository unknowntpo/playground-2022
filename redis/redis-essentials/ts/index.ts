// https://github.com/redis/node-redis

import { createClient } from 'redis';

const client = createClient();

async function main() {
  client.on('error', err => console.log('Redis Client Error', err));

  await client.connect();

  await client.set('key', 'value');
  const value = await client.get('key');
  console.log(`Get Value: ${value}`)
  await client.disconnect();
}

main()

