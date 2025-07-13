package org.example;

import org.example.service.EcommerceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

class EcommerceServiceTest {

    private EcommerceService ecommerceService;

    @BeforeAll
    static void setUpAll() {
        // Ensure shared container is started
        TestContainerConfig.getSharedContainer();
        System.out.println("🧪 Shared RabbitMQ container started and ready for tests");
    }

    @BeforeEach
    void setUp() {
        String rabbitmqHost = TestContainerConfig.getRabbitmqHost();
        int rabbitmqPort = TestContainerConfig.getRabbitmqPort();
        
        ecommerceService = new EcommerceService(rabbitmqHost, rabbitmqPort);
        
        System.out.println("🧪 Test setup - RabbitMQ running at " + rabbitmqHost + ":" + rabbitmqPort);
    }

    @AfterEach
    void tearDown() {
        if (ecommerceService != null) {
            ecommerceService.shutdown();
        }
    }

    @Test
    void shouldProcessOrdersWithMultipleWorkers() {
        // Given
        int workers = 2;
        int customers = 2;
        int ordersPerCustomer = 3;
        int expectedTotalOrders = customers * ordersPerCustomer;

        // When
        ecommerceService.startWarehouseWorkers(workers);
        ecommerceService.startOrderProducers(customers, ordersPerCustomer);

        // Then - wait for all orders to be processed
        await()
            .atMost(Duration.ofSeconds(30))
            .pollInterval(Duration.ofSeconds(1))
            .until(() -> ecommerceService.getTotalProcessedOrders() >= expectedTotalOrders);

        assertThat(ecommerceService.getTotalProcessedOrders()).isEqualTo(expectedTotalOrders);
        assertThat(ecommerceService.getProcessors()).hasSize(workers);
        assertThat(ecommerceService.isRunning()).isTrue();
    }

    @Test
    void shouldDistributeOrdersAcrossWorkers() {
        // Given
        int workers = 3;
        int customers = 1;
        int ordersPerCustomer = 6; // More orders than workers

        // When
        ecommerceService.startWarehouseWorkers(workers);
        ecommerceService.startOrderProducers(customers, ordersPerCustomer);

        // Then - wait for all orders to be processed
        await()
            .atMost(Duration.ofSeconds(30))
            .pollInterval(Duration.ofSeconds(1))
            .until(() -> ecommerceService.getTotalProcessedOrders() >= ordersPerCustomer);

        // Verify orders were distributed (at least 2 workers should have processed orders)
        long workersWithOrders = ecommerceService.getProcessors().stream()
            .mapToInt(processor -> processor.getProcessedOrders())
            .filter(count -> count > 0)
            .count();

        assertThat(workersWithOrders).isGreaterThanOrEqualTo(2);
        assertThat(ecommerceService.getTotalProcessedOrders()).isEqualTo(ordersPerCustomer);
    }

    @Test
    void shouldHandleNoOrders() {
        // Given
        int workers = 2;

        // When
        ecommerceService.startWarehouseWorkers(workers);
        
        // Wait a bit to ensure workers are started
        await()
            .atMost(Duration.ofSeconds(5))
            .until(() -> ecommerceService.getProcessors().size() == workers);

        // Then
        assertThat(ecommerceService.getTotalProcessedOrders()).isEqualTo(0);
        assertThat(ecommerceService.getProcessors()).hasSize(workers);
        assertThat(ecommerceService.isRunning()).isTrue();
    }

    @Test
    void shouldShutdownGracefully() {
        // Given
        ecommerceService.startWarehouseWorkers(2);
        
        // When
        ecommerceService.shutdown();

        // Then
        await()
            .atMost(Duration.ofSeconds(15))
            .until(() -> !ecommerceService.isRunning());

        assertThat(ecommerceService.isRunning()).isFalse();
    }
}