#!/bin/bash
set -Eeuo pipefail

echo "hello mongoinit"
while ! mongosh --host mongo1:27017 --eval "print('MongoDB is ready')"; do sleep 1; done
while ! mongosh --host mongo2:27017 --eval "print('MongoDB is ready')"; do sleep 1; done
while ! mongosh --host mongo3:27017 --eval "print('MongoDB is ready')"; do sleep 1; done
mongosh --host mongo1:27017 --eval  'db = (new Mongo("mongo1:27017")).getDB("myDb"); config = { "_id" : "myrs", "members" : [
  {
    "_id" : 0,
    "host" : "mongo1:27017"
  },
  {
    "_id" : 1,
    "host" : "mongo2:27017"
  },
  {
    "_id" : 2,
    "host" : "mongo3:27017"
  }
] }; rs.initiate(config)'