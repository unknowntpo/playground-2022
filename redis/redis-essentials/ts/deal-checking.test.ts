// https://github.com/redis/node-redis

// import * as redis from "redis";
import { createClient, RedisClientType, RedisFlushModes } from "redis";
import { isMainThread } from "worker_threads";
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

  test("if deal has already sent to the user", async () => {
    // client.ping
    await client.ping()
    console.log("ping ok")

    const dealID: string = "deal:1"
    const userID: string = "1"

    await sendDealIfNotSent(client, dealID, userID)

    const isMember: boolean = await client.sIsMember(dealID, userID)
    expect(isMember).toEqual(true)
  })
})


// class dealSender {
//   dealID: {}
//   client: RedisClientType

//   constructor(dealID: number, client: RedisClientType) {
//     this.dealID = dealID
//     this.client = client
//   }


//   public sendDealIfNotSent(dealID: , userID) {

//   }
// }

async function sendDealIfNotSent(client: RedisClientType, dealID: string, userID: string) {
  const isMember: boolean = await client.sIsMember(dealID, userID)
  if (isMember) {
    console.log(`Deal ${dealID} was already sent to user ${userID}`)
    return
  }
  markDealAsSent(client, dealID, userID)
}

function markDealAsSent(client: RedisClientType, dealID: string, userID: string) {
  client.sAdd(dealID, userID)
}


