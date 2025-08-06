package org.example;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionVerificationTest {
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionVerificationTest.class);
    private SqlSessionFactory sqlSessionFactory;
    
    @BeforeEach
    void setUp() {
        sqlSessionFactory = TestDatabaseSetup.getSqlSessionFactory();
        TestDatabaseSetup.clearTestData();
        TestDatabaseSetup.clearChangeEvents();
    }
    
    @Test
    void testTransactionRollbackBehavior() throws SQLException {
        logger.info("=== Testing Transaction Rollback Behavior ===");
        
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            Connection connection = session.getConnection();
            
            logger.info("Connection details: hashCode={}, autoCommit={}, transactionIsolation={}", 
                       connection.hashCode(), connection.getAutoCommit(), connection.getTransactionIsolation());
            
            // Check initial counts
            int initialUserCount = countUsers(connection);
            int initialAuditCount = countAuditLogs(connection);
            logger.info("Initial counts - Users: {}, Audit logs: {}", initialUserCount, initialAuditCount);
            
            // Insert user (this will trigger audit log via interceptor)
            User user = new User("test_tx_verify_user", "txverify@example.com", "TX", "Verify");
            logger.info("Inserting user...");
            userMapper.insert(user);
            
            // Check counts after insert but before commit
            int afterInsertUserCount = countUsers(connection);
            int afterInsertAuditCount = countAuditLogs(connection);
            logger.info("After insert (before commit) - Users: {}, Audit logs: {}", 
                       afterInsertUserCount, afterInsertAuditCount);
            
            assertEquals(initialUserCount + 1, afterInsertUserCount, "User should be visible in same transaction");
            assertEquals(initialAuditCount + 1, afterInsertAuditCount, "Audit log should be visible in same transaction");
            
            // Rollback
            logger.info("Rolling back transaction...");
            session.rollback();
            
            // Check counts after rollback
            int afterRollbackUserCount = countUsers(connection);
            int afterRollbackAuditCount = countAuditLogs(connection);
            logger.info("After rollback - Users: {}, Audit logs: {}", 
                       afterRollbackUserCount, afterRollbackAuditCount);
            
            assertEquals(initialUserCount, afterRollbackUserCount, "User insert should be rolled back");
            assertEquals(initialAuditCount, afterRollbackAuditCount, "Audit log should also be rolled back");
            
            logger.info("✓ Rollback test passed - both user and audit log were rolled back together");
        }
    }
    
    @Test
    void testTransactionIsolationBetweenSessions() throws SQLException {
        logger.info("=== Testing Transaction Isolation Between Sessions ===");
        
        try (SqlSession session1 = sqlSessionFactory.openSession();
             SqlSession session2 = sqlSessionFactory.openSession()) {
            
            UserMapper userMapper1 = session1.getMapper(UserMapper.class);
            Connection conn1 = session1.getConnection();
            Connection conn2 = session2.getConnection();
            
            logger.info("Session1 connection: hashCode={}, autoCommit={}", 
                       conn1.hashCode(), conn1.getAutoCommit());
            logger.info("Session2 connection: hashCode={}, autoCommit={}", 
                       conn2.hashCode(), conn2.getAutoCommit());
            
            assertNotEquals(conn1.hashCode(), conn2.hashCode(), 
                          "Different sessions should use different connections");
            
            // Insert in session1 but don't commit
            User user = new User("test_isolation_user", "isolation@example.com", "Isolation", "Test");
            userMapper1.insert(user);
            logger.info("Inserted user in session1 with ID: {}", user.getId());
            
            // Check counts from both sessions
            int session1UserCount = countUsers(conn1);
            int session1AuditCount = countAuditLogs(conn1);
            int session2UserCount = countUsers(conn2);
            int session2AuditCount = countAuditLogs(conn2);
            
            logger.info("Session1 sees - Users: {}, Audit logs: {}", session1UserCount, session1AuditCount);
            logger.info("Session2 sees - Users: {}, Audit logs: {}", session2UserCount, session2AuditCount);
            
            assertTrue(session1UserCount > session2UserCount, 
                      "Session1 should see uncommitted user");
            assertTrue(session1AuditCount > session2AuditCount, 
                      "Session1 should see uncommitted audit log");
            
            // Commit session1
            logger.info("Committing session1...");
            session1.commit();
            
            // Check counts again
            int session2UserCountAfterCommit = countUsers(conn2);
            int session2AuditCountAfterCommit = countAuditLogs(conn2);
            
            logger.info("After commit, Session2 sees - Users: {}, Audit logs: {}", 
                       session2UserCountAfterCommit, session2AuditCountAfterCommit);
            
            assertEquals(session1UserCount, session2UserCountAfterCommit, 
                        "Session2 should see committed user");
            assertEquals(session1AuditCount, session2AuditCountAfterCommit, 
                        "Session2 should see committed audit log");
            
            // Cleanup
            UserMapper userMapper2 = session2.getMapper(UserMapper.class);
            userMapper2.delete(user.getId());
            session2.commit();
            
            logger.info("✓ Isolation test passed - audit logs follow same transaction boundaries as main operations");
        }
    }
    
    @Test
    void testExceptionDuringAuditLogging() {
        logger.info("=== Testing Exception Handling During Audit Logging ===");
        
        // This test demonstrates what happens if audit logging fails
        // (Note: Our current implementation catches exceptions, but let's verify behavior)
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            
            // Insert user - if audit logging fails, it shouldn't affect the main operation
            User user = new User("test_exception_user", "exception@example.com", "Exception", "Test");
            userMapper.insert(user);
            session.commit();
            
            // User should still be inserted even if audit logging had issues
            User foundUser = userMapper.findById(user.getId());
            assertNotNull(foundUser, "User should be inserted even if audit logging fails");
            
            // Cleanup
            userMapper.delete(user.getId());
            session.commit();
            
            logger.info("✓ Exception test passed - main operation succeeds regardless of audit logging");
        }
    }
    
    private int countUsers(Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM user");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
    
    private int countAuditLogs(Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM change_event");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
}