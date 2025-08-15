package org.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.example.resource.RoleResource;
import org.example.resource.UserResource;

public class App {
    
    public static void main(String[] args) throws Exception {
        ResourceConfig config = new ResourceConfig();
        config.register(RoleResource.class);
        config.register(UserResource.class);
        
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(server, "/api");
        context.addServlet(servlet, "/*");
        
        try {
            server.start();
            System.out.println("MyBatis + Caffeine Cache Demo Server started on port 8080");
            System.out.println("Available endpoints:");
            System.out.println("- GET /api/roles");
            System.out.println("- GET /api/roles/{id}");
            System.out.println("- GET /api/roles/{id}/with-users");
            System.out.println("- GET /api/users");
            System.out.println("- GET /api/users/{id}");
            System.out.println("- GET /api/users/{id}/with-roles");
            System.out.println("- DELETE /api/users/{id}");
            System.out.println("- DELETE /api/roles/{roleId}/users/{userId}");
            
            server.join();
        } finally {
            server.destroy();
        }
    }
}
