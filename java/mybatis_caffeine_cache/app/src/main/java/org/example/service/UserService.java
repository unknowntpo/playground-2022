package org.example.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.config.MyBatisConfig;
import org.example.entity.User;
import org.example.mapper.UserMapper;

import java.util.List;

public class UserService {

    private final SqlSessionFactory sqlSessionFactory;

    public UserService() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    public User getUserById(Long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.getUserById(id);
        }
    }

    public User getUserByIdWithRoles(Long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.getUserByIdWithRoles(id);
        }
    }

    public List<User> getAllUsers() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.getAllUsers();
        }
    }

    public List<User> getUsersByRoleId(Long roleId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.getUsersByRoleId(roleId);
        }
    }

    public void createUser(User user) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            mapper.insertUser(user);
            session.commit();
        }
    }

    public void updateUser(User user) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            mapper.updateUser(user);
            session.commit();
            
            // Clear the role cache since user info affects role-with-users queries
            sqlSessionFactory.getConfiguration().getCache("org.example.mapper.RoleMapper").clear();
        } catch (Exception e) {
            // If cache clearing fails, log but don't fail the operation
            System.err.println("Warning: Failed to clear cache: " + e.getMessage());
            try (SqlSession session = sqlSessionFactory.openSession()) {
                UserMapper mapper = session.getMapper(UserMapper.class);
                mapper.updateUser(user);
                session.commit();
            }
        }
    }

    public void deleteUser(Long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            
            mapper.removeUserFromAllRoles(id);
            mapper.deleteUser(id);
            session.commit();
            
            // Clear the global cache by accessing the configuration
            sqlSessionFactory.getConfiguration().getCache("org.example.mapper.RoleMapper").clear();
        } catch (Exception e) {
            // If cache clearing fails, log but don't fail the operation
            System.err.println("Warning: Failed to clear cache: " + e.getMessage());
            try (SqlSession session = sqlSessionFactory.openSession()) {
                UserMapper mapper = session.getMapper(UserMapper.class);
                mapper.removeUserFromAllRoles(id);
                mapper.deleteUser(id);
                session.commit();
            }
        }
    }

    public void removeUserFromAllRoles(Long userId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            mapper.removeUserFromAllRoles(userId);
            session.commit();
        }
    }
}