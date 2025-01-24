package org.example.messagings.kafka;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.configs.kafka.KafkaTopicConfig;
import org.example.entities.ClickEvent;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class ClickEventConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClickEventConsumer.class);

    public static final List<ClickEvent> buffer = new ArrayList<>();

    @KafkaListener(topics = KafkaTopicConfig.CLICK_EVENT_TOPIC_NAME)
    public void listenClickEvents(ClickEvent message) {
        LOGGER.info("Received Message from {}: {}", KafkaTopicConfig.CLICK_EVENT_TOPIC_NAME, message);
        buffer.add(message);
    }
}
