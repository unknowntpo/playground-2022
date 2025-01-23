package org.example.messagings.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.configs.kafka.KafkaTopicConfig;
import org.example.entities.ClickEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClickEventConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClickEventConsumer.class);
    @KafkaListener(topics = KafkaTopicConfig.CLICK_EVENT_TOPIC_NAME)
    public void listenClickEvents(ClickEvent message) {
        LOGGER.info("Received Message from {}: {}", KafkaTopicConfig.CLICK_EVENT_TOPIC_NAME, message);
    }
}
