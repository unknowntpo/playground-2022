package org.example;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.mapper.UserMapper;
import org.example.mapper.ProductMapper;
import org.example.model.User;
import org.example.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    
    private SqlSessionFactory sqlSessionFactory;
    
    @BeforeEach
    void setUp() {
        sqlSessionFactory = TestDatabaseSetup.getSqlSessionFactory();
        TestDatabaseSetup.clearTestData();
        TestDatabaseSetup.clearChangeEvents();
    }
    
    @Test
    void testApplicationIntegration() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            ProductMapper productMapper = session.getMapper(ProductMapper.class);
            
            int initialAuditCount = TestDatabaseSetup.getChangeEventCount();
            
            User user = new User("integration_user", "integration@example.com", "Integration", "Test");
            userMapper.insert(user);
            
            Product product = new Product("Integration Product", "Test product", 
                                        new BigDecimal("199.99"), 5);
            productMapper.insert(product);
            
            session.commit();
            
            assertNotNull(user.getId(), "User should have generated ID");
            assertNotNull(product.getId(), "Product should have generated ID");
            
            int finalAuditCount = TestDatabaseSetup.getChangeEventCount();
            assertEquals(initialAuditCount + 2, finalAuditCount, 
                        "Should have 2 audit entries (user insert + product insert)");
            
            userMapper.delete(user.getId());
            productMapper.delete(product.getId());
            session.commit();
        }
    }
    
    @Test
    void testDatabaseConnection() {
        assertNotNull(sqlSessionFactory, "SqlSessionFactory should be initialized");
        
        try (SqlSession session = sqlSessionFactory.openSession()) {
            var connection = session.getConnection();
            assertFalse(connection.isClosed(), "Database connection should be open");
        } catch (Exception e) {
            fail("Should be able to establish database connection: " + e.getMessage());
        }
    }
}
