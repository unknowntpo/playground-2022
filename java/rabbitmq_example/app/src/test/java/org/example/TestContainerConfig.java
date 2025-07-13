package org.example;

import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.time.Duration;

public class TestContainerConfig {
    
    private static final DockerComposeContainer<?> SHARED_CONTAINER;
    private static volatile boolean containerStarted = false;
    
    static {
        SHARED_CONTAINER = new DockerComposeContainer<>(new File("../docker-compose.yml"))
                .withExposedService("rabbitmq", 5672)
                .withExposedService("rabbitmq", 15672)
                .waitingFor("rabbitmq", Wait.forListeningPort().withStartupTimeout(Duration.ofMinutes(2)))
                .waitingFor("rabbitmq", Wait.forLogMessage(".*Server startup complete.*", 1)
                        .withStartupTimeout(Duration.ofMinutes(2)));
    }
    
    public static synchronized DockerComposeContainer<?> getSharedContainer() {
        // Start container only once
        if (!containerStarted) {
            SHARED_CONTAINER.start();
            containerStarted = true;
        }
        return SHARED_CONTAINER;
    }
    
    public static String getRabbitmqHost() {
        return getSharedContainer().getServiceHost("rabbitmq", 5672);
    }
    
    public static int getRabbitmqPort() {
        return getSharedContainer().getServicePort("rabbitmq", 5672);
    }
    
    public static int getRabbitmqManagementPort() {
        return getSharedContainer().getServicePort("rabbitmq", 15672);
    }
    
    public static String getRabbitmqConnectionString() {
        return getRabbitmqHost() + ":" + getRabbitmqPort();
    }
    
    // Generic methods to get any service connection details
    public static String getServiceHost(String serviceName, int port) {
        return getSharedContainer().getServiceHost(serviceName, port);
    }
    
    public static int getServicePort(String serviceName, int port) {
        return getSharedContainer().getServicePort(serviceName, port);
    }
    
    public static String getServiceConnectionString(String serviceName, int port) {
        return getServiceHost(serviceName, port) + ":" + getServicePort(serviceName, port);
    }
}