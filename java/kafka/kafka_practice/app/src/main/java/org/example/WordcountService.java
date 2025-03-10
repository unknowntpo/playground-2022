package org.example;

import com.google.common.collect.ImmutableMap;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class WordcountService {
    private static final Logger LOG = LoggerFactory.getLogger(WordcountService.class);
    private static final String TOPIC_NAME = "topic1";
    private KafkaProducer producer;
    private KafkaConsumer<String, String> consumer;
    private AdminClient adminClient;
    private NewTopic topic;
    private static final int MESSAGE_COUNT = 300;
    Map<String, Integer> messageMap = ImmutableMap.of("Alpha", MESSAGE_COUNT, "Beta", MESSAGE_COUNT, "Gama", MESSAGE_COUNT);

    public WordcountService() throws ExecutionException, InterruptedException {
        String bootstrapServers = "localhost:9092";
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);

        this.adminClient = AdminClient.create(props);
        this.resetTopic(adminClient);

        // Configure Kafka Producer properties
        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", bootstrapServers);
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("acks", "all");

        this.producer = new KafkaProducer(producerProps);

        // Configure Kafka Consumer properties
        Properties consumerProps = new Properties();
        consumerProps.put("bootstrap.servers", bootstrapServers);
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("group.id", "wordcount-consumer-group");
        consumerProps.put("auto.offset.reset", "earliest"); // Start from the beginning of the topic
        consumerProps.put("enable.auto.commit", "true");

        // Create the consumer
        this.consumer = new KafkaConsumer<>(consumerProps);

        // Subscribe to the topic
        consumer.subscribe(Collections.singletonList(topic.name()));
    }

    private void createTopic(AdminClient adminClient) {
        NewTopic topic = new NewTopic(TOPIC_NAME, 2, (short) 1);
        adminClient.createTopics(Collections.singletonList(topic));
        this.topic = topic;
    }

    public void produce() {
        messageMap.forEach((key, count) -> {
            for (int i = 0; i < count; i++) {
                producer.send(
                        new ProducerRecord<String, String>(topic.name(),
                                key,
                                String.format("random value: %f", Math.random())));
            }
            LOG.info("key: {}, sent {} records", key, count);
        });
    }

    public void consume() {
        Map<String, Integer>count = new HashMap<>();

        LOG.info("in consume ...");

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                if (records.isEmpty()) {
                    return;
                }
                for (ConsumerRecord<String, String> record : records) {
                    LOG.info("Consumed: offset = {}, key = {}, value = {}",
                            record.offset(), record.key(), record.value());
                    // Process the message here
                    count.put(record.key(), count.getOrDefault(record.key(), 0) + 1);
                }
            }
        } finally {
            LOG.info("Finally, count: {}", count);
            consumer.close();
        }
    }

    private void resetTopic(AdminClient adminClient) throws ExecutionException, InterruptedException {
        try {
            DeleteTopicsResult result = adminClient.deleteTopics(Collections.singletonList(TOPIC_NAME));
            result.all().get();
        } catch (Exception e) {
        }

        createTopic(adminClient);
    }
}
