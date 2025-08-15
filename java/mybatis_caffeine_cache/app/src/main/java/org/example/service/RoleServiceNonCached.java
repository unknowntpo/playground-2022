package org.example.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.config.MyBatisConfig;
import org.example.entity.Role;
import org.example.mapper.RoleMapperNonCached;

public class RoleServiceNonCached {

    private final SqlSessionFactory sqlSessionFactory;

    public RoleServiceNonCached() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    public Role getRoleById(Long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            RoleMapperNonCached mapper = session.getMapper(RoleMapperNonCached.class);
            return mapper.getRoleById(id);
        }
    }

    public Role getRoleByIdWithUsers(Long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            RoleMapperNonCached mapper = session.getMapper(RoleMapperNonCached.class);
            return mapper.getRoleByIdWithUsers(id);
        }
    }
}