package org.example.kstream;

import org.apache.kafka.common.requests.OffsetCommitResponse;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DlqExampleTest {
    private static final String INPUT_TOPIC = "input-topic";
    private static final String OUTPUT_TOPIC = "output-topic";
    private TopologyTestDriver testDriver;
    private TestInputTopic<String, Integer> inputTopic;
    private TestOutputTopic<String, Boolean> outputTopic;

    @BeforeEach
    void setUp() {
        testDriver = new TopologyTestDriver(buildTopology(), getProps());
        inputTopic = testDriver.createInputTopic(INPUT_TOPIC, Serdes.String().serializer(), Serdes.Integer().serializer());
        outputTopic = testDriver.createOutputTopic(OUTPUT_TOPIC, Serdes.String().deserializer(), Serdes.Boolean().deserializer());
    }


    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    private Topology buildTopology() {
        var builder = new StreamsBuilder();
        KStream<String, Integer> source = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), Serdes.Integer()));
        source.mapValues(value -> value % 2 == 0)
              .to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Boolean()));
        return builder.build();
    }

    @Test
    void testSimple() {
        inputTopic.pipeKeyValueList(List.of(KeyValue.pair("key1", 1), KeyValue.pair("key2", 2), KeyValue.pair("key3", 3)));
        var results = outputTopic.readKeyValuesToList();
        assertEquals(results, List.of(KeyValue.pair("key1", false), KeyValue.pair("key2", true), KeyValue.pair("key3", false)));
    }

    public static Properties getProps() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "dlq-example");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return props;
    }
}