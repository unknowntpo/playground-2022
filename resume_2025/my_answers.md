# My Interview Answers

## Question 1: System Design & Performance (HTTP Streaming)

**Your Answer:**
```
[Write your detailed answer here about the 100s -> 5s latency improvement]
1. In Lawsnote. Our product is a Taiwan law database.
We use web crawler to periodically crawling law act information from governments website.
There's a act version control service in our team, which allow legal professionals to edit and modify act in our database.

During the edition, while displaying the diff,
an act is divided into several sections. e.g. act content, history of the act, ...
each section of law is computed separately.
Sometimes for a section with very long text. It takes 100s to compute the result.  
So user experience is bad, it needs to wait for whole content to be computed.

2. We introduce http streaming, which can deliver computed section, minimize first byte latency.

3. Typings problem of swagger package.

4. Since the difference is huge, we just manually observe it.

```

- To be reviewed:
    - [ ] concept of Transform, backpressure in NodeJs.
    - [ ] reproduce this feature with hono server.
- keywords:
    - Civil Code (民法)
    - Criminal Law
---

## Question 2: SQL Performance Optimization  

**Your Answer:**
```
[Write your detailed answer here about the 60s -> 1s improvement using Index-Only Scan]
Mediatek: chip design
Our service is a data warehouse
- Run simulation, compute the power consumption of chip
- We built our own version control system to version the data

key1 -> 100
key2 -> 300

version 3: 300, get all keys which tstmp >= 300, rank 1 
version 2: 200, get all keys which tstmp >= 200, rank 1 

The sql 

https://blog.unknowntpo.me/idx-only-scan/

```
1. Slow because sequential scan of full table.
2. When SQL query only needs columns inside a index, we can scan the index to get result  
3. 4. I use EXPLAIN statement

---

## Question 3: Open Source Contribution & JVM

**Your Answer:**
```
[Write your detailed answer here about the JVM container awareness problem in Kafka]
```
1. When JVM started, it will allocate 1/4 of total memory in your machine for the heap.
If JVM is in a container whose memory is limited by cgroup, say 1G, we expect max heap it to be 0.25G
But when we use `-verbose:gc -XX:+PrintGCDetails`, we can observe that it allocated more then 0.25G  
2. 

https://issues.apache.org/jira/browse/KAFKA-17343

---

TODO:
- [ ] Understand how gc works in JVM.

## Question 4: Memory Optimization

**Your Answer:**
```
[Write your detailed answer here about sync.Pool and 50% memory reduction]
```

---

## Question 5: Microservice Architecture Design

**Your Answer:**
```
[Write your detailed answer here about message queue based microservice architecture]
```

---

## Question 6: Data Engineering & Monitoring

**Your Answer:**
```
[Write your detailed answer here about MinIO events and Elasticsearch monitoring system]
```
