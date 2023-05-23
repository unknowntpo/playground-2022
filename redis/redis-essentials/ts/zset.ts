// https://github.com/redis/node-redis

import { createClient } from 'redis';

const client = createClient();

class LeaderBoard {
  private key: string

  constructor(key: string) {
    this.key = key
  }

  async addUser(username: string, score: number) {
    try {
      await client.zAdd(this.key, { score: score, value: username, })
    } catch (err) {
      console.log(err)
      return
    }
    console.log(`User ${username} with score ${score} added to the leaderboard`)
  }

  async removeUser(username: string) {
    try {
      await client.zRem(this.key, username)
    } catch (err) {
      console.log(err)
      return
    }
    console.log(`user ${username} removed successfully.`)
  }

  async getUserScoreAndRank(username: string) {
    await client.zScore(this.key, username)
      .catch((err) => console.error(err))
      .then((res) => console.log(`The score of ${username} is ${res}`))
    // TODO: How to get both score and rank? 
  }

}

/*
LeaderBoard.prototype.getUserScoreAndRank = function(username) { // 1 
  var leaderboardKey = this.key; // 2
  client.zscore(leaderboardKey, username, function(err, zscoreReply) { // 3
    client.zrevrank(leaderboardKey, username, function( err, zrevrankReply) { // 4
      console.log("\nDetails of " + username + ":"); 
      console.log("Score:", zscoreReply + ", Rank: #" + (zrevrankReply + 1)); // 5 
    });
  }); 
};

LeaderBoard.prototype.showTopUsers = function(quantity) { // 1 
  client.zrevrange([this.key, 0, quantity - 1, "WITHSCORES"], function(err, reply) { // 2
    console.log("\nTop", quantity, "users:");
    for (var i = 0, rank = 1 ; i < reply.length ; i += 2, rank++) {// 3 
      console.log("#" + rank, "User: " + reply[i] + ", score:", reply[i + 1]); 
    }
  }); 
};


LeaderBoard.prototype.getUsersAroundUser = function(username, quantity, callback) { // 1
  var leaderboardKey = this.key; // 2
  client.zrevrank(leaderboardKey, username, function(err, zrevrankReply) { // 3
    var startOffset = Math.floor(zrevrankReply - (quantity / 2) + 1); // 4
    if (startOffset < 0) { // 5
      startOffset = 0;
    }
    var endOffset = startOffset + quantity - 1; // 6
    client.zrevrange([leaderboardKey, startOffset, endOffset, "WITHSCORES"], function(err, zrevrangeReply) { // 7
      var users = []; // 8
      for (var i = 0, rank = 1 ; i < zrevrangeReply.length ; i += 2, rank++) { // 9 
        var user = {
          rank: startOffset + rank,
          score: zrevrangeReply[i + 1],
          username: zrevrangeReply[i],
        }; // 10
        users.push(user); // 11 
      }
      callback(users); // 12
    }); 
  });
};
*/

async function main() {
  client.on('error', err => console.log('Redis Client Error', err));

  await client.connect();

  await client.set('key', 'value');
  const value = await client.get('key');
  console.log(`Get Value: ${value} `)

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

  await client.disconnect();
}

/*
var leaderBoard = new LeaderBoard("game-score");
leaderBoard.addUser("Arthur", 70);
leaderBoard.addUser("KC", 20);
leaderBoard.addUser("Maxwell", 10);
leaderBoard.addUser("Patrik", 30);
leaderBoard.addUser("Ana", 60);
leaderBoard.addUser("Felipe", 40);
leaderBoard.addUser("Renata", 50);
leaderBoard.addUser("Hugo", 80);
leaderBoard.removeUser("Arthur");
leaderBoard.getUserScoreAndRank("Maxwell");
leaderBoard.showTopUsers(3);
leaderBoard.getUsersAroundUser("Felipe", 5, function(users) { // 1 
  console.log("\nUsers around Felipe:"); 
  users.forEach(function(user) {
    console.log("#" + user.rank, "User:", user.username + ", score:", user.score);
  });
  client.quit(); // 2 
});
*/

main()
