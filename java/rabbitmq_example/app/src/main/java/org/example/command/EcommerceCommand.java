package org.example.command;

import org.example.service.EcommerceService;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Scanner;

@Command(
    name = "ecommerce",
    aliases = {"ec"},
    description = "E-commerce Order Processing Demo (Work Queues Pattern)",
    mixinStandardHelpOptions = true
)
public class EcommerceCommand implements Runnable {

    @Option(names = {"-w", "--workers"}, description = "Number of warehouse workers (default: 3)")
    private int workers = 3;

    @Option(names = {"-c", "--customers"}, description = "Number of customers placing orders (default: 2)")
    private int customers = 2;

    @Option(names = {"-o", "--orders"}, description = "Orders per customer (default: 5)")
    private int ordersPerCustomer = 5;

    @Option(names = {"--auto"}, description = "Run automatically without user input")
    private boolean autoMode = false;

    private EcommerceService ecommerceService;

    @Override
    public void run() {
        System.out.println("=== 📦 E-commerce Order Processing System ===");
        System.out.println("Amazon-style order processing using RabbitMQ work queues");
        System.out.println("Make sure RabbitMQ server is running on localhost:5672");
        System.out.println("Start RabbitMQ: docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management");
        System.out.println();
        System.out.println("Configuration:");
        System.out.println("  🏭 Warehouse workers: " + workers);
        System.out.println("  🛒 Customers: " + customers);
        System.out.println("  📦 Orders per customer: " + ordersPerCustomer);
        System.out.println();

        ecommerceService = new EcommerceService();
        ecommerceService.startWarehouseWorkers(workers);

        if (autoMode) {
            System.out.println("🤖 Auto mode: Starting order generation...");
            ecommerceService.startOrderProducers(customers, ordersPerCustomer);

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            System.out.println("Press ENTER to start sending orders, or 'q' to quit:");
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String input = scanner.nextLine().trim();

                if ("q".equalsIgnoreCase(input)) {
                    break;
                } else if (input.isEmpty()) {
                    ecommerceService.startOrderProducers(customers, ordersPerCustomer);
                } else {
                    System.out.println("Press ENTER to send more orders, or 'q' to quit:");
                }
            }
            scanner.close();
        }

        ecommerceService.shutdown();
    }
}
