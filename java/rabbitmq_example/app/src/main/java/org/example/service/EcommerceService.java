package org.example.service;

import org.example.consumer.OrderProcessor;
import org.example.producer.OrderProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EcommerceService {
    private final List<OrderProcessor> processors = new ArrayList<>();
    private ExecutorService executorService;
    private final String rabbitmqHost;
    private final int rabbitmqPort;

    public EcommerceService() {
        this("localhost", 5672);
    }

    public EcommerceService(String rabbitmqHost, int rabbitmqPort) {
        this.rabbitmqHost = rabbitmqHost;
        this.rabbitmqPort = rabbitmqPort;
    }

    public void startWarehouseWorkers(int workers) {
        if (executorService == null) {
            executorService = Executors.newCachedThreadPool();
        }
        
        System.out.println("🏭 Starting " + workers + " warehouse workers...");
        
        for (int i = 1; i <= workers; i++) {
            OrderProcessor worker = new OrderProcessor("Warehouse-Worker-" + i, rabbitmqHost, rabbitmqPort);
            processors.add(worker);
            executorService.submit(worker);
        }
        
        System.out.println("✅ " + workers + " warehouse workers are ready and waiting for orders!");
    }

    public void startOrderProducers(int customers, int ordersPerCustomer) {
        if (executorService == null) {
            executorService = Executors.newCachedThreadPool();
        }
        
        System.out.println("🛒 " + customers + " customers are placing orders...");
        
        for (int i = 1; i <= customers; i++) {
            OrderProducer customer = new OrderProducer("Customer-" + i, ordersPerCustomer, rabbitmqHost, rabbitmqPort);
            executorService.submit(customer);
        }
        
        int totalOrders = customers * ordersPerCustomer;
        System.out.println("📦 Sending " + totalOrders + " orders total (" + ordersPerCustomer + " from each customer)");
        System.out.println("Watch how orders are distributed among warehouse workers in round-robin fashion!");
    }

    public void shutdown() {
        System.out.println("\n🛑 Shutting down the system...");
        
        for (OrderProcessor processor : processors) {
            processor.stop();
        }
        
        if (executorService != null) {
            executorService.shutdown();
            
            try {
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("📊 Final Statistics:");
        int totalProcessed = 0;
        for (OrderProcessor processor : processors) {
            totalProcessed += processor.getProcessedOrders();
        }
        System.out.println("Total orders processed: " + totalProcessed);
        System.out.println("✅ E-commerce demo complete!");
    }

    public int getTotalProcessedOrders() {
        return processors.stream().mapToInt(OrderProcessor::getProcessedOrders).sum();
    }

    public List<OrderProcessor> getProcessors() {
        return new ArrayList<>(processors);
    }

    public boolean isRunning() {
        return executorService != null && !executorService.isShutdown();
    }
}