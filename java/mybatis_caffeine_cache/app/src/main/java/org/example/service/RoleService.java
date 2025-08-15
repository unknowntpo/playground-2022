package org.example.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.config.MyBatisConfig;
import org.example.entity.Role;
import org.example.mapper.RoleMapper;

import java.util.List;

public class RoleService {

    private final SqlSessionFactory sqlSessionFactory;

    public RoleService() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    public Role getRoleById(Long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            RoleMapper mapper = session.getMapper(RoleMapper.class);
            return mapper.getRoleById(id);
        }
    }

    public Role getRoleByIdWithUsers(Long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            RoleMapper mapper = session.getMapper(RoleMapper.class);
            return mapper.getRoleByIdWithUsers(id);
        }
    }

    public List<Role> getAllRoles() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            RoleMapper mapper = session.getMapper(RoleMapper.class);
            return mapper.getAllRoles();
        }
    }

    public void createRole(Role role) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            RoleMapper mapper = session.getMapper(RoleMapper.class);
            mapper.insertRole(role);
            session.commit();
        }
    }

    public void updateRole(Role role) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            RoleMapper mapper = session.getMapper(RoleMapper.class);
            mapper.updateRole(role);
            session.commit();
        }
    }

    public void deleteRole(Long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            RoleMapper mapper = session.getMapper(RoleMapper.class);
            mapper.deleteRole(id);
            session.commit();
        }
    }

    public void removeUserFromRole(Long userId, Long roleId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            RoleMapper mapper = session.getMapper(RoleMapper.class);
            
            mapper.removeUserFromRole(userId, roleId);
            session.commit();
            
            // Clear the global cache by accessing the configuration
            sqlSessionFactory.getConfiguration().getCache("org.example.mapper.RoleMapper").clear();
        } catch (Exception e) {
            // If cache clearing fails, log but don't fail the operation
            System.err.println("Warning: Failed to clear cache: " + e.getMessage());
            try (SqlSession session = sqlSessionFactory.openSession()) {
                RoleMapper mapper = session.getMapper(RoleMapper.class);
                mapper.removeUserFromRole(userId, roleId);
                session.commit();
            }
        }
    }

    public void addUserToRole(Long userId, Long roleId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            RoleMapper mapper = session.getMapper(RoleMapper.class);
            
            mapper.addUserToRole(userId, roleId);
            session.commit();
            
            // Clear the global cache by accessing the configuration
            sqlSessionFactory.getConfiguration().getCache("org.example.mapper.RoleMapper").clear();
        } catch (Exception e) {
            // If cache clearing fails, log but don't fail the operation
            System.err.println("Warning: Failed to clear cache: " + e.getMessage());
            try (SqlSession session = sqlSessionFactory.openSession()) {
                RoleMapper mapper = session.getMapper(RoleMapper.class);
                mapper.addUserToRole(userId, roleId);
                session.commit();
            }
        }
    }
}