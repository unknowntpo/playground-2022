package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Order {
    @JsonProperty("orderId")
    private int orderId;
    
    @JsonProperty("customerId")
    private int customerId;
    
    @JsonProperty("items")
    private List<String> items;
    
    @JsonProperty("totalAmount")
    private double totalAmount;
    
    @JsonProperty("timestamp")
    private long timestamp;

    public Order() {
    }

    public Order(int orderId, int customerId, List<String> items, double totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.timestamp = System.currentTimeMillis();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("Order{orderId=%d, customerId=%d, items=%s, totalAmount=%.2f, timestamp=%d}", 
                orderId, customerId, items, totalAmount, timestamp);
    }
}