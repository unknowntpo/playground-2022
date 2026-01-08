package org.example.kstream;


import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KStreamExampleTest {
    private TopologyTestDriver testDriver;
    private TestInputTopic<String, String> inputTopic;
    private TestOutputTopic<String, Long> outputTopic;

    @BeforeEach
    void setUp() {
        testDriver = new TopologyTestDriver(KStreamExample.buildTopology(), KStreamExample.getProps());
        inputTopic = testDriver.createInputTopic("input-topic", Serdes.String().serializer(), Serdes.String().serializer());
        outputTopic = testDriver.createOutputTopic("output-topic", Serdes.String().deserializer(), Serdes.Long().deserializer());
    }

    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    @Test
    void testGroupByCount() {
        inputTopic.pipeInput("apple", "v1");
        inputTopic.pipeInput("apple", "v2");
        inputTopic.pipeInput("banana", "v1");

        var results = outputTopic.readKeyValuesToMap();
        assertThat(results.get("apple")).isEqualTo(2L);
        assertThat(results.get("banana")).isEqualTo(1L);
    }
}