package org.example.messagings.kafka;

import jakarta.annotation.Resource;
import org.example.configs.kafka.ClickEventTopic;
import org.example.entities.ClickEvent;
import org.springframework.kafka.core.KafkaTemplate;

public class ClickEventProducer {
    @Resource
    private KafkaTemplate<String, ClickEvent> kafkaTemplate;

    public void sendEvent(ClickEvent event) {
        kafkaTemplate.send(ClickEventTopic.TOPIC_NAME, event);
    }
}
