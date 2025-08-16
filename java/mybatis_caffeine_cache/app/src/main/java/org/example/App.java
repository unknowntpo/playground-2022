package org.example;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.example.config.JacksonConfig;

import jakarta.ws.rs.core.UriBuilder;
import java.net.URI;

public class App {

    public static void main(String[] args) {
        try {
            ResourceConfig config = new ResourceConfig();
            config.packages("org.example.controller");
            config.register(JacksonFeature.class);
            config.register(JacksonConfig.class);

            // Debug: List registered resources
            System.out.println("Registered classes: " + config.getClasses());
            System.out.println("Registered instances: " + config.getInstances());

            URI baseUri = UriBuilder.fromUri("http://localhost/")
                    .port(8080)
                    .build();

            Server server = JettyHttpContainerFactory.createServer(baseUri, config);

            System.out.println("MyBatis + Caffeine Cache Demo Server started at: " + baseUri);
            System.out.println("API Endpoints:");
            System.out.println("  GET  /api/roles - Get all roles");
            System.out.println("  GET  /api/roles/{id} - Get role by ID");
            System.out.println("  GET  /api/roles/{id}/with-users - Get role with users");
            System.out.println("  GET  /api/users - Get all users");
            System.out.println("  GET  /api/users/{id} - Get user by ID");
            System.out.println("  GET  /api/users/{id}/with-roles - Get user with roles");
            System.out.println("  DELETE /api/users/{id} - Delete user");
            System.out.println("  DELETE /api/roles/{roleId}/users/{userId} - Remove user from role");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("Shutting down server...");
                    server.stop();
                } catch (Exception e) {
                    System.err.println("Error during shutdown: " + e.getMessage());
                }
            }));

            server.join();

        } catch (Exception e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
