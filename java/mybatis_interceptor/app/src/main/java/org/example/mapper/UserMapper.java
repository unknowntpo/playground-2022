package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.model.User;

import java.util.List;

@Mapper
public interface UserMapper {
    
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);
    
    @Select("SELECT * FROM user")
    List<User> findAll();
    
    @Insert("INSERT INTO user (username, email, first_name, last_name) VALUES (#{username}, #{email}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);
    
    @Update("UPDATE user SET username = #{username}, email = #{email}, first_name = #{firstName}, last_name = #{lastName} WHERE id = #{id}")
    void update(User user);
    
    @Delete("DELETE FROM user WHERE id = #{id}")
    void delete(Long id);
}