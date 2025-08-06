package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.model.Product;

import java.util.List;

@Mapper
public interface ProductMapper {
    
    @Select("SELECT * FROM product WHERE id = #{id}")
    Product findById(Long id);
    
    @Select("SELECT * FROM product")
    List<Product> findAll();
    
    @Insert("INSERT INTO product (name, description, price, stock_quantity) VALUES (#{name}, #{description}, #{price}, #{stockQuantity})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Product product);
    
    @Update("UPDATE product SET name = #{name}, description = #{description}, price = #{price}, stock_quantity = #{stockQuantity} WHERE id = #{id}")
    void update(Product product);
    
    @Delete("DELETE FROM product WHERE id = #{id}")
    void delete(Long id);
}