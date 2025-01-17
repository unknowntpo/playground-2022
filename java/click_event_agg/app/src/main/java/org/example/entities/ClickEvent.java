package org.example.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ClickEvent implements Serializable {
    private final long timestamp;
    @JsonProperty("user_id")
    private Integer userId;
    private String tag;

    public ClickEvent(Integer userId, String tag) {
        this.userId = userId;
        this.tag = tag;
        this.timestamp = System.currentTimeMillis();
    }
//
//    @Override
//    public String toString() {
//        return String.format("%d,%d,%s", timestamp, userId, tag);
//    }
}
