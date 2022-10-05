local article = "article"
local votes = "votes"
-- local head = "head-line"
--

-- function upVote(id)

local function upVote(id)
    local key = article .. ":" .. id .. ":"  .. votes 
    redis.call("INCR", key)
end
-- function downVote(id)

local function downVote(id)
    local key = article .. ":" .. id .. ":" .. votes
    redis.call("DECR", key)
end

-- function showResults(id)
local function showResults(id)
    local voteKey = article .. ":" .. id .. ":"  .. votes 
    -- How log to stdout ?
    redis.log(redis.LOG_WARNING, "The article" .. id .. "has" .. redis.call("GET", voteKey) .. "votes")
end

upVote(12345) -- article:12345 has 1 vote
upVote(12345) -- article:12345 has 2 votes
upVote(12345) -- article:12345 has 3 votes
upVote(10001) -- article:10001 has 1 vote
upVote(10001) -- article:10001 has 2 votes
downVote(10001) -- article:10001 has 1 vote
upVote(60056) -- article:60056 has 1 vote
showResults(12345)
showResults(10001)
showResults(60056)
