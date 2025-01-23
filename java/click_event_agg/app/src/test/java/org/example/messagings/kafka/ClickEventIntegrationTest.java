package org.example.messagings.kafka;

import jakarta.annotation.Resource;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.example.configs.kafka.KafkaConsumerConfig;
import org.example.configs.kafka.KafkaProducerConfig;
import org.example.configs.kafka.KafkaTopicConfig;
import org.example.entities.ClickEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@SpringBootTest(
        classes = {ClickEventProducer.class, ClickEventConsumer.class, KafkaTopicConfig.class, KafkaProducerConfig.class, KafkaConsumerConfig.class}
)
@Testcontainers
class ClickEventIntegrationTest {
    static ConfluentKafkaContainer kafka = new ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0")).withReuse(true);
    @Autowired
    private DefaultKafkaConsumerFactory<?, ?> kafkaConsumerFactory;

    @BeforeAll
    static void setup() {
        kafka.start();
    }

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Resource
    private KafkaTemplate<String, ClickEvent> kafkaTemplate;

    @Autowired
    KafkaTopicConfig kafkaTopicConfig;

    @Autowired
    private ClickEventProducer producer;

    @Autowired
    private ClickEventConsumer consumer;

    @Resource
    private ConcurrentKafkaListenerContainerFactory<String, ClickEvent> kafkaListenerContainerFactory;

    @Test
    void testProducerAndConsumer() throws Exception {
        String topic = KafkaTopicConfig.CLICK_EVENT_TOPIC_NAME;
        // Create and start the Kafka listener container using the factory
        ContainerProperties containerProperties = new ContainerProperties(topic);

        Consumer kafkaConsumer = kafkaConsumerFactory.createConsumer();

        // Subscribe to the topic
        kafkaConsumer.subscribe(Collections.singletonList(topic));

        ClickEvent event = new ClickEvent((int) (Math.random() * 10), "Test message");


        // Produce a message
        for (int i = 0; i < 100; i++) {
            producer.sendEvent(event);
        }

        // Consume the message
        ConsumerRecords<String, ClickEvent> records = kafkaConsumer.poll(Duration.ofSeconds(10));

        for (ConsumerRecord<String, ClickEvent> record : records) {
            System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }

        // Get metadata for the topic
        List<PartitionInfo> partitions = kafkaConsumer.partitionsFor(topic);

        // Print the metadata
        for (PartitionInfo partition : partitions) {
            System.out.println("Topic: " + partition.topic());
            System.out.println("Partition: " + partition.partition());
            System.out.println("Leader: " + partition.leader());
            System.out.println("Replicas: " + java.util.Arrays.toString(partition.replicas()));
            System.out.println("In-Sync Replicas: " + java.util.Arrays.toString(partition.inSyncReplicas()));
            System.out.println("----------------------------------------");
        }


//        ConsumerRecord<String, ClickEvent> record = records.iterator().next();
//        assertEquals(event, record.value());
        kafkaConsumer.close();
    }
}
