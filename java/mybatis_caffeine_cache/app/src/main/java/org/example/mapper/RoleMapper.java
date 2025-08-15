package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.example.entity.Role;

import java.util.List;

@Mapper
@CacheNamespace(implementation = org.mybatis.caches.caffeine.CaffeineCache.class,
                properties = {
                    @Property(name = "maximumSize", value = "1000"),
                    @Property(name = "expireAfterWrite", value = "300"),
                    @Property(name = "expireAfterAccess", value = "60")
                })
public interface RoleMapper {
    
    @Select("SELECT id, name, description, created_at, updated_at FROM roles WHERE id = #{id}")
    @Results(id = "roleResult", value = {
        @Result(property = "id", column = "id", id = true),
        @Result(property = "name", column = "name"),
        @Result(property = "description", column = "description"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    Role getRoleById(@Param("id") Long id);
    
    @Select("SELECT id, name, description, created_at, updated_at FROM roles WHERE id = #{id}")
    @Results(id = "roleWithUsersResult", value = {
        @Result(property = "id", column = "id", id = true),
        @Result(property = "name", column = "name"),
        @Result(property = "description", column = "description"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "users", column = "id", many = @Many(
            select = "getUsersByRoleId",
            fetchType = FetchType.EAGER
        ))
    })
    Role getRoleByIdWithUsers(@Param("id") Long id);
    
    @Select("SELECT u.id, u.name, u.email, u.created_at, u.updated_at " +
            "FROM users u " +
            "INNER JOIN user_roles ur ON u.id = ur.user_id " +
            "WHERE ur.role_id = #{roleId} " +
            "ORDER BY u.name")
    @Results({
        @Result(property = "id", column = "id", id = true),
        @Result(property = "name", column = "name"),
        @Result(property = "email", column = "email"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<org.example.entity.User> getUsersByRoleId(@Param("roleId") Long roleId);
    
    @Select("SELECT id, name, description, created_at, updated_at FROM roles ORDER BY name")
    @ResultMap("roleResult")
    List<Role> getAllRoles();
    
    @Insert("INSERT INTO roles (name, description) VALUES (#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Flush
    void insertRole(Role role);
    
    @Update("UPDATE roles SET name = #{name}, description = #{description}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    @Flush
    void updateRole(Role role);
    
    @Delete("DELETE FROM roles WHERE id = #{id}")
    @Flush
    void deleteRole(@Param("id") Long id);
    
    @Delete("DELETE FROM user_roles WHERE user_id = #{userId} AND role_id = #{roleId}")
    @Flush
    void removeUserFromRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
    
    @Insert("INSERT INTO user_roles (user_id, role_id) VALUES (#{userId}, #{roleId})")
    @Flush
    void addUserToRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
}