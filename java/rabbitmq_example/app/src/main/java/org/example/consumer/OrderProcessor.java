package org.example.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.example.model.Order;

import java.io.IOException;
import java.util.Random;

public class OrderProcessor implements Runnable {
    private final String queueName = "order_queue";
    private final String workerName;
    private final String rabbitmqHost;
    private final int rabbitmqPort;
    private final ObjectMapper objectMapper;
    private final Random random;
    private volatile boolean running = true;
    private int processedOrders = 0;

    public OrderProcessor(String workerName) {
        this(workerName, "localhost", 5672);
    }

    public OrderProcessor(String workerName, String rabbitmqHost, int rabbitmqPort) {
        this.workerName = workerName;
        this.rabbitmqHost = rabbitmqHost;
        this.rabbitmqPort = rabbitmqPort;
        this.objectMapper = new ObjectMapper();
        this.random = new Random();
    }

    @Override
    public void run() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitmqHost);
        factory.setPort(rabbitmqPort);
        factory.setUsername("admin");
        factory.setPassword("password");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            
            channel.queueDeclare(queueName, true, false, false, null);
            
            channel.basicQos(1);
            
            System.out.println(String.format("[%s] Warehouse worker started, waiting for orders...", workerName));
            
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    String orderJson = new String(delivery.getBody(), "UTF-8");
                    Order order = objectMapper.readValue(orderJson, Order.class);
                    
                    System.out.println(String.format("[%s] Received order: %s", workerName, order));
                    
                    processOrder(order);
                    
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    processedOrders++;
                    
                    System.out.println(String.format("[%s] Completed order %d (Total processed: %d)", 
                            workerName, order.getOrderId(), processedOrders));
                    
                } catch (Exception e) {
                    System.err.println(String.format("[%s] Error processing order: %s", workerName, e.getMessage()));
                    channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                }
            };
            
            CancelCallback cancelCallback = consumerTag -> {
                System.out.println(String.format("[%s] Consumer cancelled", workerName));
            };
            
            channel.basicConsume(queueName, false, deliverCallback, cancelCallback);
            
            while (running) {
                Thread.sleep(1000);
            }
            
        } catch (Exception e) {
            System.err.println(String.format("[%s] Error in order processor: %s", workerName, e.getMessage()));
            e.printStackTrace();
        }
        
        System.out.println(String.format("[%s] Warehouse worker stopped. Total orders processed: %d", 
                workerName, processedOrders));
    }
    
    private void processOrder(Order order) throws InterruptedException {
        int processingTime = 2000 + random.nextInt(3000);
        
        System.out.println(String.format("[%s] Processing order %d with %d items (estimated time: %dms)", 
                workerName, order.getOrderId(), order.getItems().size(), processingTime));
        
        Thread.sleep(processingTime);
        
        System.out.println(String.format("[%s] ✓ Order %d packaged and ready for shipping! Items: %s, Total: $%.2f", 
                workerName, order.getOrderId(), order.getItems(), order.getTotalAmount()));
    }
    
    public void stop() {
        running = false;
    }
    
    public int getProcessedOrders() {
        return processedOrders;
    }
}