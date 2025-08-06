package org.example.interceptor;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.TestDatabaseSetup;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionBehaviorTest {
    
    private SqlSessionFactory sqlSessionFactory;
    
    @BeforeEach
    void setUp() {
        sqlSessionFactory = TestDatabaseSetup.getSqlSessionFactory();
        TestDatabaseSetup.clearTestData();
        TestDatabaseSetup.clearChangeEvents();
    }
    
    @Test
    void testAuditLogRollbackWithMainTransaction() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            
            int initialUserCount = userMapper.findAll().size();
            int initialAuditCount = TestDatabaseSetup.getChangeEventCount();
            
            // Insert user - this should create an audit log
            User user = new User("test_rollback_user", "rollback@example.com", "Test", "Rollback");
            userMapper.insert(user);
            
            // Verify the user exists in the session but hasn't been committed yet
            User foundUser = userMapper.findById(user.getId());
            assertNotNull(foundUser, "User should exist in the current session");
            
            // Check audit count within the same transaction
            int auditCountAfterInsert = TestDatabaseSetup.getChangeEventCount();
            assertEquals(initialAuditCount + 1, auditCountAfterInsert, "Audit log should be created in same transaction");
            
            // Rollback the transaction
            session.rollback();
            
            // Verify both user and audit log are rolled back
            int finalUserCount = userMapper.findAll().size();
            int finalAuditCount = TestDatabaseSetup.getChangeEventCount();
            
            assertEquals(initialUserCount, finalUserCount, "User insert should be rolled back");
            assertEquals(initialAuditCount, finalAuditCount, "Audit log should also be rolled back");
            
        }
    }
    
    @Test
    void testAuditLogCommitWithMainTransaction() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            
            int initialAuditCount = TestDatabaseSetup.getChangeEventCount();
            
            // Insert user
            User user = new User("test_commit_user", "commit@example.com", "Test", "Commit");
            userMapper.insert(user);
            
            // Commit the transaction
            session.commit();
            
            // Verify both user and audit log are committed
            User foundUser = userMapper.findById(user.getId());
            int finalAuditCount = TestDatabaseSetup.getChangeEventCount();
            
            assertNotNull(foundUser, "User should be committed to database");
            assertEquals(initialAuditCount + 1, finalAuditCount, "Audit log should be committed too");
            
            // Cleanup
            userMapper.delete(user.getId());
            session.commit();
        }
    }
    
    @Test 
    void testTransactionIsolationBehavior() {
        // Test what happens when we create a separate session during transaction
        try (SqlSession session1 = sqlSessionFactory.openSession();
             SqlSession session2 = sqlSessionFactory.openSession()) {
            
            UserMapper userMapper1 = session1.getMapper(UserMapper.class);
            UserMapper userMapper2 = session2.getMapper(UserMapper.class);
            
            int initialAuditCount = TestDatabaseSetup.getChangeEventCount();
            
            // Insert user in session1 but don't commit
            User user = new User("test_isolation_user", "isolation@example.com", "Test", "Isolation");
            userMapper1.insert(user);
            
            // Check if session2 can see the uncommitted user (it shouldn't due to isolation)
            User foundInSession2 = userMapper2.findById(user.getId());
            assertNull(foundInSession2, "User should not be visible in other session before commit");
            
            // Check audit count from session2 - should not see the audit log yet
            int auditCountFromSession2 = TestDatabaseSetup.getChangeEventCount();
            assertEquals(initialAuditCount, auditCountFromSession2, 
                        "Audit log should not be visible in other session before commit");
            
            // Commit session1
            session1.commit();
            
            // Now session2 should be able to see both user and audit log
            User foundAfterCommit = userMapper2.findById(user.getId());
            int finalAuditCount = TestDatabaseSetup.getChangeEventCount();
            
            assertNotNull(foundAfterCommit, "User should be visible after commit");
            assertEquals(initialAuditCount + 1, finalAuditCount, 
                        "Audit log should be visible after commit");
            
            // Cleanup
            userMapper2.delete(user.getId());
            session2.commit();
        }
    }
}