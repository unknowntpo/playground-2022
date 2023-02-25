local redis = require "redis-client"

local client = redis.connect("localhost:6379")

client.set("hello-lua", "abc")

local value = client.get("foo")

print(value)

