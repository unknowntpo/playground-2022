package org.example;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class MixedLifecycleTests {

    // will be shared between test methods
    @Container
    private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8.0");
    // will be started before and stopped after each test method
    @Container
    private static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("foo")
            .withUsername("foo")
            .withPassword("secret");

    @Test
    void test() {
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertTrue(postgresqlContainer.isRunning());
        assertEquals(1 + 1, 2);
    }

    @AfterAll
    static void cleanup() {
        MY_SQL_CONTAINER.close();
        postgresqlContainer.close();
    }
}

