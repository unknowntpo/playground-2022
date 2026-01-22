# Kafka upgrade from 3.9.0 to 4.0.0 playground

- Use docker-compose.yml to host all kafka instances
- 3 nodes, each start from 3.9.0, and upgrade to 4.0.0
- rolling upgrade, we need a producer and consumer to make sure no downtime with k6.

## Tasks

- [x] Create `docker-compose.yml` with 3-node KRaft cluster (3.9.0) using YAML anchors.
- [x] Create `k6/script.js` for continuous producer/consumer traffic.
- [ ] Start the 3.9.0 cluster and verify traffic.
- [ ] Upgrade Node 1 to 4.0.0 and verify availability.
- [ ] Upgrade Node 2 to 4.0.0 and verify availability.
- [ ] Upgrade Node 3 to 4.0.0 and verify availability.
- [ ] Update Metadata Version to 4.0 using `kafka-features.sh`.
    - Command: `kafka-features.sh --bootstrap-server localhost:9092 upgrade --feature metadata.version=20` (Note: 4.0 usually maps to a specific version number, check `kafka-features.sh describe` first).

## References

- [Kafka 4.0.0 Release Notes](https://archive.apache.org/dist/kafka/4.0.0/RELEASE_NOTES.html)
- [Kafka Upgrade Guide (Official)](https://kafka.apache.org/documentation/#upgrade)
- [KRaft Metadata Management](https://kafka.apache.org/documentation/#kraft_metadata)
