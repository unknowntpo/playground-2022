package org.acme;

import io.quarkus.redis.client.RedisClient;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractIntegrationTest {

    @Inject
    EntityManager entityManager;

    @Inject
    RedisClient redisClient;

    @BeforeEach
    @Transactional
    public void cleanup() {
        // Truncate all tables
        entityManager.createNativeQuery("TRUNCATE TABLE products RESTART IDENTITY CASCADE").executeUpdate();

        // Flush Redis cache
        redisClient.flushall(java.util.List.of());
    }
}
