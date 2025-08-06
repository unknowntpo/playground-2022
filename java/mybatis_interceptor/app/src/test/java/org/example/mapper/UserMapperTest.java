package org.example.mapper;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.TestDatabaseSetup;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {
    
    private SqlSessionFactory sqlSessionFactory;
    private UserMapper userMapper;
    private SqlSession session;
    
    @BeforeEach
    void setUp() {
        sqlSessionFactory = TestDatabaseSetup.getSqlSessionFactory();
        session = sqlSessionFactory.openSession();
        userMapper = session.getMapper(UserMapper.class);
        TestDatabaseSetup.clearTestData();
        TestDatabaseSetup.clearChangeEvents();
    }
    
    @Test
    void testInsertAndFindById() {
        User user = new User("test_john_doe", "test_john@example.com", "John", "Doe");
        
        userMapper.insert(user);
        session.commit();
        
        assertNotNull(user.getId(), "User ID should be generated after insert");
        
        User foundUser = userMapper.findById(user.getId());
        assertNotNull(foundUser, "Should find inserted user");
        assertEquals("test_john_doe", foundUser.getUsername());
        assertEquals("test_john@example.com", foundUser.getEmail());
        assertEquals("John", foundUser.getFirstName());
        assertEquals("Doe", foundUser.getLastName());
        
        userMapper.delete(user.getId());
        session.commit();
    }
    
    @Test
    void testFindAll() {
        List<User> initialUsers = userMapper.findAll();
        int initialCount = initialUsers.size();
        
        User user1 = new User("test_user1", "test_user1@example.com", "User", "One");
        User user2 = new User("test_user2", "test_user2@example.com", "User", "Two");
        
        userMapper.insert(user1);
        userMapper.insert(user2);
        session.commit();
        
        List<User> allUsers = userMapper.findAll();
        assertEquals(initialCount + 2, allUsers.size(), "Should have two more users");
        
        userMapper.delete(user1.getId());
        userMapper.delete(user2.getId());
        session.commit();
    }
    
    @Test
    void testUpdate() {
        User user = new User("test_original_user", "test_original@example.com", "Original", "User");
        userMapper.insert(user);
        session.commit();
        
        user.setUsername("test_updated_user");
        user.setEmail("updated@example.com");
        user.setFirstName("Updated");
        user.setLastName("Name");
        
        userMapper.update(user);
        session.commit();
        
        User updatedUser = userMapper.findById(user.getId());
        assertEquals("test_updated_user", updatedUser.getUsername());
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals("Updated", updatedUser.getFirstName());
        assertEquals("Name", updatedUser.getLastName());
        
        userMapper.delete(user.getId());
        session.commit();
    }
    
    @Test
    void testDelete() {
        User user = new User("test_to_delete", "test_delete@example.com", "To", "Delete");
        userMapper.insert(user);
        session.commit();
        
        Long userId = user.getId();
        assertNotNull(userMapper.findById(userId), "User should exist before delete");
        
        userMapper.delete(userId);
        session.commit();
        
        assertNull(userMapper.findById(userId), "User should not exist after delete");
    }
    
    void cleanup() {
        if (session != null) {
            session.close();
        }
    }
}