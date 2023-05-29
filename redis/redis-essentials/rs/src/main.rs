/*
var redis = require("redis");
var client = redis.createClient();

function markDealAsSent(dealId, userId) { // 1
  client.sadd(dealId, userId); // 2
}

function sendDealIfNotSent(dealId, userId) { // 1
  client.sismember(dealId, userId, function(err, reply) { // 2
    if (reply) {
      console.log("Deal", dealId, "was already sent to user", userId); // 3
    } else {
      console.log("Sending", dealId, "to user", userId); // 4
      // code to send the deal to the user would go here... // 5
      markDealAsSent(dealId, userId); // 6
    }
  });
}

function showUsersThatReceivedAllDeals(dealIds) { // 1
  client.sinter(dealIds, function(err, reply) { // 2
    console.log(reply + " received all of the deals: " + dealIds); // 3
  });
}

function showUsersThatReceivedAtLeastOneOfTheDeals(dealIds) { // 1
  client.sunion(dealIds, function(err, reply) { // 2
    console.log(reply + " received at least one of the deals: " + dealIds); // 3
  });
}

markDealAsSent('deal:1', 'user:1');
markDealAsSent('deal:1', 'user:2');
markDealAsSent('deal:2', 'user:1');
markDealAsSent('deal:2', 'user:3');
sendDealIfNotSent('deal:1', 'user:1');
sendDealIfNotSent('deal:1', 'user:2');
sendDealIfNotSent('deal:1', 'user:3');
showUsersThatReceivedAllDeals(["deal:1", "deal:2"]);
showUsersThatReceivedAtLeastOneOfTheDeals(["deal:1", "deal:2"]);

client.quit();
*/

extern crate redis;
use redis::{Client, Commands, Connection, RedisResult};

fn fetch_an_integer(client: redis::Client) -> redis::RedisResult<isize> {
    let mut con = client.get_connection()?;

    let my_key: String = "my_key".to_string();
    let _: () = con.set(&my_key, 42)?;

    con.get(my_key)
}

fn main() {
    println!("Hello, world!");
    let client = redis::Client::open("redis://127.0.0.1/").unwrap();
    let mut con: Connection = client.get_connection().unwrap();

    let deal_ids = vec!["deal:1".to_string(), "deal:2".to_string()];
    let user_ids = vec![
        "user:1".to_string(),
        "user:2".to_string(),
        "user:3".to_string(),
    ];

    for deal_id in &deal_ids {
        for user_id in &user_ids {
            mark_deal_as_sent(&mut con, deal_id, user_id).unwrap();
        }
    }
    for deal_id in &deal_ids {
        for user_id in &user_ids {
            send_deal_if_not_sent(&mut con, deal_id, user_id).unwrap();
        }
    }

    show_users_that_received_all_deals(&mut con, &deal_ids).unwrap();
    show_users_that_received_at_least_one_of_the_deals(&mut con, &deal_ids).unwrap();
    println!("{}", fetch_an_integer(client).unwrap());
}

fn mark_deal_as_sent(con: &mut Connection, deal_id: &str, user_id: &str) -> RedisResult<()> {
    let _: () = con.sadd(deal_id, user_id)?;
    Ok(())
}

fn send_deal_if_not_sent(con: &mut Connection, deal_id: &str, user_id: &str) -> RedisResult<()> {
    let is_member: bool = con.sismember(deal_id, user_id)?;

    if is_member {
        println!("Deal {} was already sent to user {}", deal_id, user_id);
    } else {
        println!("Sending {} to user {}", deal_id, user_id);
        // code to send the deal to the user would go here...
        mark_deal_as_sent(con, deal_id, user_id)?;
    }
    Ok(())
}

fn show_users_that_received_all_deals(
    con: &mut Connection,
    deal_ids: &Vec<String>,
) -> RedisResult<()> {
    //    let users: Vec<String> = con.sinter(deal_ids.to_owned())?;
    let users: Vec<String> = con.sinter(deal_ids.to_owned())?;
    println!(
        "{} received all of the deals: {:?}",
        users.join(", "),
        deal_ids
    );
    Ok(())
}

fn show_users_that_received_at_least_one_of_the_deals(
    con: &mut Connection,
    deal_ids: &Vec<String>,
) -> RedisResult<()> {
    let users: Vec<String> = con.sunion(deal_ids.to_owned())?;
    println!(
        "{} received at least one of the deals: {:?}",
        users.join(", "),
        deal_ids
    );
    Ok(())
}
