package org.example.kstream;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DlqExampleTest {
    private static final String INPUT_TOPIC = "input-topic";
    private static final String ODD_OUTPUT_TOPIC = "odd-output-topic";
    private static final String EVEN_OUTPUT_TOPIC = "event-output-topic";
    private TopologyTestDriver testDriver;
    private TestInputTopic<String, Integer> inputTopic;
    private TestOutputTopic<String, Integer> oddOutputTopic;
    private TestOutputTopic<String, Integer> eventOutputTopic;

    @BeforeEach
    void setUp() {
        testDriver = new TopologyTestDriver(buildTopology(), getProps());
        inputTopic = testDriver.createInputTopic(INPUT_TOPIC, Serdes.String().serializer(), Serdes.Integer().serializer());
        oddOutputTopic = testDriver.createOutputTopic(ODD_OUTPUT_TOPIC, Serdes.String().deserializer(), Serdes.Integer().deserializer());
        eventOutputTopic = testDriver.createOutputTopic(EVEN_OUTPUT_TOPIC, Serdes.String().deserializer(), Serdes.Integer().deserializer());
    }


    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    @Test
    void testSplitBranch() {
        inputTopic.pipeKeyValueList(List.of(KeyValue.pair("key1", 1), KeyValue.pair("key2", 2), KeyValue.pair("key3", 3)));
        var oddResults = oddOutputTopic.readKeyValuesToList();
        var evenResults = eventOutputTopic.readKeyValuesToList();
        assertEquals(oddResults, List.of(KeyValue.pair("key1", 1), KeyValue.pair("key3", 3)));
        assertEquals(evenResults, List.of(KeyValue.pair("key2", 2)));
    }

    private Topology buildTopology() {
        var builder = new StreamsBuilder();
        KStream<String, Integer> source = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), Serdes.Integer()));
        source.split()
                .branch(
                        (key, value) -> value % 2 != 0,
                        Branched.withConsumer(ks -> ks.to(ODD_OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Integer()))
                        ))
                .defaultBranch(
                        Branched.withConsumer(ks -> ks.to(EVEN_OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Integer())))
                );
        return builder.build();
    }


    public static Properties getProps() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "dlq-example");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return props;
    }
}