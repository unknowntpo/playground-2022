package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties properties = new Properties();
    
    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }
    
    public static String getUrl() {
        return properties.getProperty("database.url");
    }
    
    public static String getUsername() {
        return properties.getProperty("database.username");
    }
    
    public static String getPassword() {
        return properties.getProperty("database.password");
    }
    
    public static String getDriver() {
        return properties.getProperty("database.driver");
    }
    
    public static int getServerPort() {
        return Integer.parseInt(properties.getProperty("server.port", "8080"));
    }
    
    public static String getServerHost() {
        return properties.getProperty("server.host", "localhost");
    }
}