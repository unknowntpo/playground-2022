package org.example.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.example.model.Order;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderProducer implements Runnable {
    private final String queueName = "order_queue";
    private final String producerName;
    private final int ordersToSend;
    private final String rabbitmqHost;
    private final int rabbitmqPort;
    private final ObjectMapper objectMapper;
    private final Random random;
    private static final AtomicInteger orderIdCounter = new AtomicInteger(1);
    
    private static final List<List<String>> SAMPLE_ORDERS = Arrays.asList(
            Arrays.asList("laptop", "mouse", "keyboard"),
            Arrays.asList("smartphone", "case", "charger"),
            Arrays.asList("headphones", "cable"),
            Arrays.asList("tablet", "stylus"),
            Arrays.asList("monitor", "hdmi_cable"),
            Arrays.asList("book", "bookmark"),
            Arrays.asList("coffee_maker", "coffee_beans"),
            Arrays.asList("backpack", "water_bottle")
    );
    
    private static final double[] SAMPLE_PRICES = {1299.99, 899.99, 199.99, 649.99, 399.99, 29.99, 159.99, 79.99};

    public OrderProducer(String producerName, int ordersToSend) {
        this(producerName, ordersToSend, "localhost", 5672);
    }

    public OrderProducer(String producerName, int ordersToSend, String rabbitmqHost, int rabbitmqPort) {
        this.producerName = producerName;
        this.ordersToSend = ordersToSend;
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
            
            System.out.println(String.format("[%s] Started producing orders...", producerName));
            
            for (int i = 0; i < ordersToSend; i++) {
                Order order = generateRandomOrder();
                String orderJson = objectMapper.writeValueAsString(order);
                
                channel.basicPublish("", queueName, null, orderJson.getBytes("UTF-8"));
                
                System.out.println(String.format("[%s] Sent order: %s", producerName, order));
                
                Thread.sleep(1000 + random.nextInt(2000));
            }
            
            System.out.println(String.format("[%s] Finished producing %d orders", producerName, ordersToSend));
            
        } catch (Exception e) {
            System.err.println(String.format("[%s] Error producing orders: %s", producerName, e.getMessage()));
            e.printStackTrace();
        }
    }
    
    private Order generateRandomOrder() {
        int orderIndex = random.nextInt(SAMPLE_ORDERS.size());
        List<String> items = SAMPLE_ORDERS.get(orderIndex);
        double totalAmount = SAMPLE_PRICES[orderIndex] + (random.nextDouble() * 100);
        int customerId = 1000 + random.nextInt(9000);
        
        return new Order(
                orderIdCounter.getAndIncrement(),
                customerId,
                items,
                Math.round(totalAmount * 100.0) / 100.0
        );
    }
}