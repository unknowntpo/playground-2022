package org.example.messagings.kafka;

import jakarta.annotation.Resource;
import org.example.configs.kafka.KafkaTopicConfig;
import org.example.entities.ClickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ClickEventProducer {
    @Resource
    private KafkaTemplate<String, ClickEvent> kafkaTemplate;

    public static final Logger LOGGER = LoggerFactory.getLogger(ClickEventProducer.class);

    public void sendEvent(ClickEvent event) {
        LOGGER.info("sending event to {}: {}", KafkaTopicConfig.CLICK_EVENT_TOPIC_NAME, event);
        kafkaTemplate.send(KafkaTopicConfig.CLICK_EVENT_TOPIC_NAME, event);
    }
}
