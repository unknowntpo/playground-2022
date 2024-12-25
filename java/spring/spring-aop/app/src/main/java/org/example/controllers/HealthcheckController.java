package org.example.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/healthz")
public class HealthcheckController {
    @GetMapping
    public ResponseEntity<Map<String, String>> healthcheckHandler() {
        Map<String, String> healthStatus = new HashMap<>();
        healthStatus.put("status", "ok");
        healthStatus.put("foo", "bar");
        healthStatus.put("database", "Connected");
        healthStatus.put("externalApi", "Responsive");

        return new ResponseEntity<>(healthStatus, HttpStatus.OK);
    }
}
