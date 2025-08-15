package org.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyBatisConfig {
    
    private static SqlSessionFactory sqlSessionFactory;
    private static DataSource dataSource;
    
    static {
        try {
            // Configure HikariCP DataSource
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://localhost:3309/mybatis_cache_db");
            config.setUsername("app_user");
            config.setPassword("app_password");
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);
            config.setLeakDetectionThreshold(60000);
            
            dataSource = new HikariDataSource(config);
            
            // Build SqlSessionFactory using XML configuration
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            
            // Set up properties for the datasource
            Properties props = new Properties();
            props.setProperty("driver", "com.mysql.cj.jdbc.Driver");
            props.setProperty("url", "jdbc:mysql://localhost:3309/mybatis_cache_db");
            props.setProperty("username", "app_user");
            props.setProperty("password", "app_password");
            
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            sqlSessionFactory = builder.build(inputStream, props);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize MyBatis", e);
        }
    }
    
    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
    
    public static DataSource getDataSource() {
        return dataSource;
    }
}