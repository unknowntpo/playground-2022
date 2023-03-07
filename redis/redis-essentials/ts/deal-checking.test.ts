// https://github.com/redis/node-redis

// import * as redis from "redis";
import { createClient, RedisClientType } from "redis";
// const RedisClient = redis;



/*
async function main() {
  client.on('error', err => console.log('Redis Client Error', err));

  await client.connect();

  await client.set('key', 'value');
  const value = await client.get('key');
  console.log(`Get Value: ${value}`)
  await client.disconnect();
}
*/

describe("deal-checking", () => {
  // let client: redis.RedisClient
  // const client = createClient()
  let client: RedisClientType
  beforeEach(async () => {
    client = createClient()
    await client.connect()
    console.log("connection established")
  })

  test("test ping", async () => {
    // client.ping
    await client.ping()
    console.log("ping ok")
    expect(1 + 1).toEqual(2)
  })
})

// function markDealAsSent(dealId, userId) {
//   client.sadd(dealId, userId)
// }


