package org.example.controllers;

import jakarta.annotation.Resource;
import org.example.entities.ClickEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/producer")
public class ClickGeneratorController {
    @Resource
    private KafkaTemplate<String, ClickEvent> kafkaTemplate;

    private static final Integer[] userIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    Map<String, Integer> tagsMap = Map.ofEntries(
            Map.entry("elon musk", 6),
            Map.entry("tsmc", 5),
            Map.entry("taiwan", 2),
            Map.entry("youtube", 4),
            Map.entry("spacex", 1),
            Map.entry("fire", 15),
            Map.entry("christmas", 1),
            Map.entry("twitter", 1),
            Map.entry("tiktok", 1),
            Map.entry("new year", 1),
            Map.entry("ironman", 3),
            Map.entry("japan", 1),
            Map.entry("california", 10)
    );


    @PostMapping("/generate/{count}")
    public ResponseEntity<String> generateClickEvent(@PathVariable Integer count) {
        // pick random entry
        for (int i = 0; i < count; i++) {
            tagsMap.forEach((key, value) -> {
                for (int j = 0; j < value; j++) {
                    ClickEvent fakeEvent = new ClickEvent(value, key);
                    kafkaTemplate.send("click-events", fakeEvent);
                }
            });
        }

        return ResponseEntity.ok("Ok");
    }
}
