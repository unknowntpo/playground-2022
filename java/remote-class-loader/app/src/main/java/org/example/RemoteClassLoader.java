package org.example;

import java.io.*;
import java.net.URLClassLoader;
import java.net.URL;
import java.nio.file.*;
import java.lang.reflect.*;

public class RemoteClassLoader {

    public void main(String[] args) {
        try {
            // URL of the Guava JAR file in Maven Central
            String jarUrl = "https://repo1.maven.org/maven2/com/google/guava/guava/31.1-jre/guava-31.1-jre.jar";
            
            // Local path to download the JAR file
            Path jarPath = Paths.get("guava-31.1-jre.jar");

			System.out.println(String.format("jar path: %s", jarPath));
            // Download the JAR file
            if (!Files.exists(jarPath)) {
                try (InputStream in = new URL(jarUrl).openStream()) {
                    Files.copy(in, jarPath);
                    System.out.println("Download complete: " + jarPath.toAbsolutePath());
                }
            }

            // Load the JAR file with URLClassLoader
            URL[] urls = { jarPath.toUri().toURL() };
            try (URLClassLoader classLoader = new URLClassLoader(urls)) {
                // Fully qualified class name to load
                String className = "com.google.common.base.Strings"; // Example class from Guava
                Class<?> clazz = classLoader.loadClass(className);

                // Method name to invoke
                String methodName = "isNullOrEmpty"; // Example method from Strings class

                // Get the method and invoke it
                Method method = clazz.getMethod(methodName, String.class);
                boolean result = (boolean) method.invoke(null, ""); // Invoke with empty string

                System.out.println("Result of Strings.isNullOrEmpty(\"\"): " + result);
            }

        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

