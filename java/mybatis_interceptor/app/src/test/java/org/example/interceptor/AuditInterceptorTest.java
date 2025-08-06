package org.example.interceptor;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.TestDatabaseSetup;
import org.example.mapper.UserMapper;
import org.example.mapper.ProductMapper;
import org.example.model.User;
import org.example.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class AuditInterceptorTest {
    
    private SqlSessionFactory sqlSessionFactory;
    
    @BeforeEach
    void setUp() {
        sqlSessionFactory = TestDatabaseSetup.getSqlSessionFactory();
        TestDatabaseSetup.clearTestData();
        TestDatabaseSetup.clearChangeEvents();
    }
    
    @Test
    void testUserInsertCreatesAuditLog() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            
            int initialCount = TestDatabaseSetup.getChangeEventCount();
            
            User user = new User("test_audit_user", "test_audit@example.com", "Test", "User");
            userMapper.insert(user);
            session.commit();
            
            int finalCount = TestDatabaseSetup.getChangeEventCount();
            assertEquals(initialCount + 1, finalCount, "Should create one audit log entry for insert");
            
            verifyAuditLogExists("user", "INSERT");
            
            userMapper.delete(user.getId());
            session.commit();
        }
    }
    
    @Test
    void testUserUpdateCreatesAuditLog() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            
            User user = new User("test_audit_user", "test_audit@example.com", "Test", "User");
            userMapper.insert(user);
            session.commit();
            
            TestDatabaseSetup.clearChangeEvents();
            int initialCount = TestDatabaseSetup.getChangeEventCount();
            
            user.setEmail("updated@example.com");
            userMapper.update(user);
            session.commit();
            
            int finalCount = TestDatabaseSetup.getChangeEventCount();
            assertEquals(initialCount + 1, finalCount, "Should create one audit log entry for update");
            
            verifyAuditLogExists("user", "UPDATE");
            
            userMapper.delete(user.getId());
            session.commit();
        }
    }
    
    @Test
    void testUserDeleteCreatesAuditLog() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            
            User user = new User("test_audit_user", "test_audit@example.com", "Test", "User");
            userMapper.insert(user);
            session.commit();
            
            TestDatabaseSetup.clearChangeEvents();
            int initialCount = TestDatabaseSetup.getChangeEventCount();
            
            userMapper.delete(user.getId());
            session.commit();
            
            int finalCount = TestDatabaseSetup.getChangeEventCount();
            assertEquals(initialCount + 1, finalCount, "Should create one audit log entry for delete");
            
            verifyAuditLogExists("user", "DELETE");
        }
    }
    
    @Test
    void testProductOperationsCreateAuditLogs() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ProductMapper productMapper = session.getMapper(ProductMapper.class);
            
            int initialCount = TestDatabaseSetup.getChangeEventCount();
            
            Product product = new Product("Test Product", "Description", new BigDecimal("99.99"), 10);
            productMapper.insert(product);
            session.commit();
            
            product.setPrice(new BigDecimal("89.99"));
            productMapper.update(product);
            session.commit();
            
            productMapper.delete(product.getId());
            session.commit();
            
            int finalCount = TestDatabaseSetup.getChangeEventCount();
            assertEquals(initialCount + 3, finalCount, "Should create three audit log entries (insert, update, delete)");
            
            verifyAuditLogExists("product", "INSERT");
            verifyAuditLogExists("product", "UPDATE");
            verifyAuditLogExists("product", "DELETE");
        }
    }
    
    @Test
    void testSelectOperationsDoNotCreateAuditLogs() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            
            User user = new User("test_audit_user", "test_audit@example.com", "Test", "User");
            userMapper.insert(user);
            session.commit();
            
            TestDatabaseSetup.clearChangeEvents();
            int initialCount = TestDatabaseSetup.getChangeEventCount();
            
            userMapper.findAll();
            userMapper.findById(user.getId());
            
            int finalCount = TestDatabaseSetup.getChangeEventCount();
            assertEquals(initialCount, finalCount, "Select operations should not create audit logs");
            
            userMapper.delete(user.getId());
            session.commit();
        }
    }
    
    @Test
    void testAuditLogContainsCorrectData() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            
            TestDatabaseSetup.clearChangeEvents();
            
            User user = new User("test_audit_user", "test_audit@example.com", "Test", "User");
            userMapper.insert(user);
            session.commit();
            
            verifyAuditLogData("user", "INSERT", "test_audit_user");
            
            userMapper.delete(user.getId());
            session.commit();
        }
    }
    
    private void verifyAuditLogExists(String tableName, String operationType) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            var stmt = session.getConnection().prepareStatement(
                "SELECT COUNT(*) FROM change_event WHERE table_name = ? AND operation_type = ?"
            );
            stmt.setString(1, tableName);
            stmt.setString(2, operationType);
            
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            
            assertTrue(count > 0, 
                String.format("Should have at least one audit log entry for table '%s' and operation '%s'", 
                    tableName, operationType));
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify audit log", e);
        }
    }
    
    private void verifyAuditLogData(String tableName, String operationType, String expectedDataContent) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            var stmt = session.getConnection().prepareStatement(
                "SELECT data FROM change_event WHERE table_name = ? AND operation_type = ? ORDER BY timestamp DESC LIMIT 1"
            );
            stmt.setString(1, tableName);
            stmt.setString(2, operationType);
            
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next(), "Should have audit log entry");
            
            String data = rs.getString("data");
            assertNotNull(data, "Audit log data should not be null");
            assertTrue(data.contains(expectedDataContent), 
                String.format("Audit log data should contain '%s', but was: %s", expectedDataContent, data));
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify audit log data", e);
        }
    }
}