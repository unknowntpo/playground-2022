package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.example.entity.User;

import java.util.List;

@Mapper
@CacheNamespace(implementation = org.mybatis.caches.caffeine.CaffeineCache.class,
                properties = {
                    @Property(name = "maximumSize", value = "1000"),
                    @Property(name = "expireAfterWrite", value = "300"),
                    @Property(name = "expireAfterAccess", value = "60")
                })
public interface UserMapper {
    
    @Select("SELECT id, name, email, created_at, updated_at FROM users WHERE id = #{id}")
    @Results(id = "userResult", value = {
        @Result(property = "id", column = "id", id = true),
        @Result(property = "name", column = "name"),
        @Result(property = "email", column = "email"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    User getUserById(@Param("id") Long id);
    
    @Select("SELECT u.id, u.name, u.email, u.created_at, u.updated_at, " +
            "r.id as role_id, r.name as role_name, r.description as role_description, " +
            "r.created_at as role_created_at, r.updated_at as role_updated_at " +
            "FROM users u " +
            "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
            "LEFT JOIN roles r ON ur.role_id = r.id " +
            "WHERE u.id = #{id}")
    @Results(id = "userWithRolesResult", value = {
        @Result(property = "id", column = "id", id = true),
        @Result(property = "name", column = "name"),
        @Result(property = "email", column = "email"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "roles", column = "id", many = @Many(
            select = "getRolesByUserId",
            fetchType = FetchType.EAGER
        ))
    })
    User getUserByIdWithRoles(@Param("id") Long id);
    
    @Select("SELECT r.id, r.name, r.description, r.created_at, r.updated_at " +
            "FROM roles r " +
            "INNER JOIN user_roles ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} " +
            "ORDER BY r.name")
    @Results({
        @Result(property = "id", column = "id", id = true),
        @Result(property = "name", column = "name"),
        @Result(property = "description", column = "description"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<org.example.entity.Role> getRolesByUserId(@Param("userId") Long userId);
    
    @Select("SELECT id, name, email, created_at, updated_at FROM users ORDER BY name")
    @ResultMap("userResult")
    List<User> getAllUsers();
    
    @Select("SELECT u.id, u.name, u.email, u.created_at, u.updated_at " +
            "FROM users u " +
            "INNER JOIN user_roles ur ON u.id = ur.user_id " +
            "WHERE ur.role_id = #{roleId} " +
            "ORDER BY u.name")
    @ResultMap("userResult")
    List<User> getUsersByRoleId(@Param("roleId") Long roleId);
    
    @Insert("INSERT INTO users (name, email) VALUES (#{name}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Flush
    void insertUser(User user);
    
    @Update("UPDATE users SET name = #{name}, email = #{email}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    @Flush
    void updateUser(User user);
    
    @Delete("DELETE FROM users WHERE id = #{id}")
    @Flush
    void deleteUser(@Param("id") Long id);
    
    @Delete("DELETE FROM user_roles WHERE user_id = #{userId}")
    @Flush
    void removeUserFromAllRoles(@Param("userId") Long userId);
}