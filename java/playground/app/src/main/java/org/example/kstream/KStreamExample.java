package org.example.kstream;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class KStreamExample {
    public static final String INPUT_TOPIC = "input-topic";
    public static final String OUTPUT_TOPIC = "output-topic";

    public static Properties getProps() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "groupby-example");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return props;
    }

    public static Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream(INPUT_TOPIC);
        source.groupByKey()
              .count(Materialized.as("count-store"))
              .toStream()
              .to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));
        return builder.build();
    }

    private static void createTopicsIfNeeded(Properties props) {
        try (AdminClient admin = AdminClient.create(props)) {
            Set<String> existingTopics = admin.listTopics().names().get();
            List<NewTopic> newTopics = new ArrayList<>();

            if (!existingTopics.contains(INPUT_TOPIC)) {
                newTopics.add(new NewTopic(INPUT_TOPIC, 1, (short) 1));
            }
            if (!existingTopics.contains(OUTPUT_TOPIC)) {
                newTopics.add(new NewTopic(OUTPUT_TOPIC, 1, (short) 1));
            }

            if (!newTopics.isEmpty()) {
                admin.createTopics(newTopics).all().get();
                System.out.println("Created topics: " + newTopics.stream().map(NewTopic::name).collect(Collectors.joining(", ")));
            } else {
                System.out.println("Topics already exist.");
            }
        } catch (Exception e) {
            System.err.println("Error creating topics: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Properties props = getProps();
        
        // Auto-create topics
        createTopicsIfNeeded(props);
        
        Topology topology = buildTopology();

        try (KafkaStreams streams = new KafkaStreams(topology, props)) {
            final CountDownLatch latch = new CountDownLatch(1);

            // attach shutdown handler to catch control-c
            Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
                @Override
                public void run() {
                    streams.close();
                    latch.countDown();
                }
            });

            try {
                streams.start();
                latch.await();
            } catch (Throwable e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
