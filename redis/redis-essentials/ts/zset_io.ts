import Redis from "ioredis"

const redis = new Redis()

class LeaderBoard {
  private key: string

  constructor(key: string) {
    this.key = key
  }

  async addUser(username: string, score: number) {
    await redis.zadd(this.key, score, username)
      .then(
        (res) => console.log(`User ${username} with score ${score} added to the leaderboard`)
      )
      .catch((err) => console.error(err))
  }

  async removeUser(username: string) {
    await redis.zrem(this.key, username)
      .then(
        (_) => console.log(`user ${username} removed successfully.`)
      )
      .catch((err) => console.error(err))
  }

  async getUserScoreAndRank(username: string) {
    let score: string | null = null;
    let rank: number | void | null = null;
    await Promise.all(
      [
        redis.zrank(this.key, username)
          .catch((err) => console.error(err))
          .then((res) => {
            rank = res
          }),
        redis.zscore(this.key, username)
          .then((res) => score = res)
          .catch((err) => console.error(err))
      ]
    )
    console.log(`The rank of ${username} is ${rank}, score is ${score}`)
  }

  async showTopUsers(quantity: number) {
    // zrange game-score  +inf 0 byscore rev limit 0 3 withscores

    let users: string[] = [];
    await redis.zrevrangebyscore(this.key, "+inf", 0, 'WITHSCORES', 'LIMIT', 0, quantity)
      .then((res) => users = res)
      .catch((err) => console.error(err))

    console.log(`Top ${quantity} users: ${users}`)
  }

  /*
  await Promise.all([
    client.zScore(this.key, username)
      .catch((err) => console.error(err))
      .then((res) => console.log(`The score of ${username} is ${res}`)),
    // TODO: How to get both score and rank? 
    client.zRevRank(this.key, username)
      .catch((err) => console.error(err))
      .then((res) => {
        if (res !== null) {
          console.log(`The rank of ${username} is ${res}`)
        }
      }
      )
  ])
  */
  /*
  async showTopUsers(quantity: number) {
    await client.zRange(this.key, "+inf", 0,
      {
        BY: 'SCORE',
        REV: true,
        LIMIT: { offset: 0, count: quantity },
      },
    ).catch((err) => console.error(err))
      .then((res) =>
        console.log(`The top ${quantity} of users are ${res}`)
      )
  }
  */
}


async function main() {
  //  client.on('error', err => console.log('Redis Client Error', err));

  //  await redis.connect();

  // await client.set('key', 'value');
  //  const value = await client.get('key');
  // console.log(`Get Value: ${value} `)
  //
  await redis.flushall()

  let leaderBoard = new LeaderBoard("game-score")
  await leaderBoard.addUser("Arthur", 70);
  await leaderBoard.addUser("KC", 20);
  await leaderBoard.addUser("Maxwell", 10);
  await leaderBoard.addUser("Patrik", 30);
  await leaderBoard.addUser("Ana", 60);
  await leaderBoard.addUser("Felipe", 40);
  await leaderBoard.addUser("Renata", 50);
  await leaderBoard.addUser("Hugo", 80);


  await leaderBoard.getUserScoreAndRank("Arthur")

  await leaderBoard.showTopUsers(3);

  //await client.disconnect();
}

main()
