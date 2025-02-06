package org.example.messagings.kafka;

import jakarta.annotation.Resource;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootTest(
        classes = {ClickEventProducer.class, ClickEventConsumer.class, KafkaTopicConfig.class, KafkaProducerConfig.class, KafkaConsumerConfig.class}
)
@Testcontainers
@DirtiesContext
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
    private KafkaTopicConfig kafkaTopicConfig;

    @Autowired
    private ClickEventProducer producer;

    @Autowired
    private ClickEventConsumer consumer;

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @Resource
    private ConcurrentKafkaListenerContainerFactory<String, ClickEvent> kafkaListenerContainerFactory;

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @BeforeEach
    void setupTopic() throws ExecutionException, InterruptedException {
        String topicName = KafkaTopicConfig.CLICK_EVENT_TOPIC_NAME;
        
        // 停止所有消费者
        stopConsumers();
        
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            // 删除已存在的topic
            deleteTopic(adminClient, topicName);
            
            // 使用 KafkaTopicConfig 中的配置创建新的 topic
            NewTopic newTopic = kafkaTopicConfig.clickEventTopic();
            adminClient.createTopics(Collections.singleton(newTopic)).all().get();
            
            // 等待topic创建完成
            await().atMost(10, SECONDS)
                   .until(() -> adminClient.listTopics().names().get().contains(topicName));
        }
        
        // 重启消费者
        restartConsumers();
    }

    private void deleteTopic(AdminClient adminClient, String topicName) {
        try {
            adminClient.deleteTopics(Collections.singleton(topicName)).all().get();
            await().atMost(10, SECONDS)
                   .until(() -> !adminClient.listTopics().names().get().contains(topicName));
        } catch (InterruptedException | ExecutionException e) {
            if (!(e.getCause() instanceof org.apache.kafka.common.errors.UnknownTopicOrPartitionException)) {
                throw new RuntimeException("Topic deletion failed", e);
            }
        }
    }

    private void stopConsumers() {
        registry.getListenerContainers().forEach(MessageListenerContainer::stop);
    }

    private void restartConsumers() {
        registry.getListenerContainers().forEach(MessageListenerContainer::start);
        
        // 等待所有消费者启动完成
        await().atMost(10, SECONDS)
               .until(() -> registry.getListenerContainers()
                   .stream()
                   .allMatch(MessageListenerContainer::isRunning));
    }

    @Test
    void testProducerAndConsumer() throws Exception {
        String topic = KafkaTopicConfig.CLICK_EVENT_TOPIC_NAME;
        // Create and start the Kafka listener container using the factory
        ContainerProperties containerProperties = new ContainerProperties(topic);

        List<ClickEvent> events = new ArrayList<>();

        // Produce a message
        for (int i = 0; i < 100; i++) {
            ClickEvent event = new ClickEvent((int) (Math.random() * 10), "Test message");
            producer.sendEvent(event);
            events.add(event);
        }

        Thread.sleep(1000);

        // FIXME: wait for event to be consumed
        List<ClickEvent> gotEvents = consumer.buffer;

        assertFalse(gotEvents.isEmpty());
        assertEquals(events, gotEvents);
//
//
//        // Consume the message
//        ConsumerRecords<String, ClickEvent> records = kafkaConsumer.poll(Duration.ofSeconds(10));
//
//        for (ConsumerRecord<String, ClickEvent> record : records) {
//            System.out.printf("partition = %s, offset = %d, key = %s, value = %s%n", record.partition(), record.offset(), record.key(), record.value());
//        }
//
//        // Get metadata for the topic
//        List<PartitionInfo> partitions = kafkaConsumer.partitionsFor(topic);
//
//        // Print the metadata
//        for (PartitionInfo partition : partitions) {
//            System.out.println("Topic: " + partition.topic());
//            System.out.println("Partition: " + partition.partition());
//            System.out.println("Leader: " + partition.leader());
//            System.out.println("Replicas: " + java.util.Arrays.toString(partition.replicas()));
//            System.out.println("In-Sync Replicas: " + java.util.Arrays.toString(partition.inSyncReplicas()));
//            System.out.println("----------------------------------------");
//        }
//
//
////        ConsumerRecord<String, ClickEvent> record = records.iterator().next();
////        assertEquals(event, record.value());
//        kafkaConsumer.close();
    }
}
