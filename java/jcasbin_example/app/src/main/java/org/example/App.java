package org.example;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.example.config.DatabaseConfig;
import org.example.controller.AuthController;
import org.example.controller.PolicyController;

import java.net.URI;

public class App {
    
    public static void main(String[] args) {
        try {
            ResourceConfig config = new ResourceConfig();
            config.register(AuthController.class);
            config.register(PolicyController.class);
            
            String host = DatabaseConfig.getServerHost();
            int port = DatabaseConfig.getServerPort();
            URI baseUri = URI.create("http://" + host + ":" + port + "/");
            
            Server server = JettyHttpContainerFactory.createServer(baseUri, config);
            
            System.out.println("jCasbin Demo Server started at: " + baseUri);
            System.out.println("API Endpoints:");
            System.out.println("  POST /api/auth/check - Check authorization");
            System.out.println("  GET  /api/policies - Get all policies and roles");
            System.out.println("  POST /api/policies - Add policy");
            System.out.println("  DELETE /api/policies - Remove policy");
            System.out.println("  POST /api/policies/roles - Add role assignment");
            System.out.println("  DELETE /api/policies/roles - Remove role assignment");
            System.out.println("  POST /api/policies/reload - Reload policies from database");
            
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
