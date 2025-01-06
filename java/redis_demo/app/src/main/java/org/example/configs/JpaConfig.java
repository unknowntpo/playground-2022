package org.example.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.example.repositories.jpa")
public class JpaConfig {
    // Additional configuration if needed
}

