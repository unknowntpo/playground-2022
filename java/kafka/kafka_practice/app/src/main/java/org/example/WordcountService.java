package org.example;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.random.RandomGenerator;

public class WordcountService {
	private static final Logger LOG = LoggerFactory.getLogger(WordcountService.class);
	private static final String TOPIC_NAME = "topic1";
	private KafkaProducer producer;
	private KafkaConsumer<String, String> consumer;
	private AdminClient adminClient;
	private NewTopic topic;
	private static final int MESSAGE_COUNT = 300;

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
		for (int i = 0; i < MESSAGE_COUNT; i++) {
			producer.send(
					new ProducerRecord<String, String>(topic.name(),
							String.format("key%f", Math.random()),
							String.format("random value: %f", Math.random())));
			LOG.info("Sent 1 record");
		}
	}

	public void consume() {
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
				}
			}
		} finally {
			consumer.close();
		}
	}

	private void resetTopic(AdminClient adminClient) throws ExecutionException, InterruptedException {
	try {
			DeleteTopicsResult result = adminClient.deleteTopics(Collections.singletonList(TOPIC_NAME));
		result.all().get();
		} catch (Exception e) {}

		createTopic(adminClient);
	}
}
