package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.entity.Role;

@Mapper
public interface RoleMapperNonCached {
    Role getRoleById(@Param("id") Long id);
    Role getRoleByIdWithUsers(@Param("id") Long id);
}