package producer_consumer;

import java.nio.file.*;
import java.io.*;
import java.util.*;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.producer.*;

public class ProducerExample {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Please provide the configuration file path as a command line argument");
            System.exit(1);
        }

        Properties props = loadConfig(args[0]);
        props.put("partitioner.class",
                "producer_consumer.KeyPartitioner");
        String topic = "order";
        int numPartitions = 10;
        createTopicIfNotExists(props, topic, numPartitions, 2);

        String[] orders = {
                "order0",
                "order1",
                "order2",
                "order3",
                "order4",
                "order5",
                "order6",
                "order7",
                "order8",
                "order9",
        };

        try (final Producer<String, String> producer = new KafkaProducer<>(props)) {
            for (String order : orders) {
                String key = "order";
                String value = order + "value";
                producer.send(
                        // send these records to the same partition
                        new ProducerRecord<String, String>(topic, key, order + "value"),
                        (event, ex) -> {
                            if (ex != null)
                                ex.printStackTrace();
                            else
                                System.out.printf("Produced event to topic [%s], key=%s, value=%s, partition=%d\n",
                                        topic, key,
                                        value, event.partition());
                        });
                System.out.println(order);
            }
        }
    }

    private static void createTopicIfNotExists(Properties props, String topic, int numPartitions, int repFactor) {
        try (AdminClient adminClient = AdminClient.create(props)) {
            // Check if the topic already exists
            ListTopicsResult topicsResult = adminClient.listTopics();
            Set<String> existingTopics = topicsResult.names().get();
            if (existingTopics.contains(topic)) {
                System.out.println("Topic already exists: " + topic);
            } else {
                // Create the topic with 10 partitions
                NewTopic newTopic = new NewTopic(topic, numPartitions, (short) repFactor);
                CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singletonList(newTopic));
                createTopicsResult.all().get();
                System.out.println("topic has been created successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Properties loadConfig(final String configFile) throws IOException {
        System.out.println("configFile: " + configFile);
        if (!Files.exists(Paths.get(configFile))) {
            throw new IOException(configFile + " not found.");
        }
        final Properties cfg = new Properties();
        try (InputStream inputStream = new FileInputStream(configFile)) {
            cfg.load(inputStream);
        }
        return cfg;
    }
}