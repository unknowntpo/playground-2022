package org.example.mapper;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.TestDatabaseSetup;
import org.example.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductMapperTest {
    
    private SqlSessionFactory sqlSessionFactory;
    private ProductMapper productMapper;
    private SqlSession session;
    
    @BeforeEach
    void setUp() {
        sqlSessionFactory = TestDatabaseSetup.getSqlSessionFactory();
        session = sqlSessionFactory.openSession();
        productMapper = session.getMapper(ProductMapper.class);
        TestDatabaseSetup.clearTestData();
        TestDatabaseSetup.clearChangeEvents();
    }
    
    @Test
    void testInsertAndFindById() {
        Product product = new Product("Test Laptop", "High-performance laptop", 
                                    new BigDecimal("1299.99"), 5);
        
        productMapper.insert(product);
        session.commit();
        
        assertNotNull(product.getId(), "Product ID should be generated after insert");
        
        Product foundProduct = productMapper.findById(product.getId());
        assertNotNull(foundProduct, "Should find inserted product");
        assertEquals("Test Laptop", foundProduct.getName());
        assertEquals("High-performance laptop", foundProduct.getDescription());
        assertEquals(new BigDecimal("1299.99"), foundProduct.getPrice());
        assertEquals(Integer.valueOf(5), foundProduct.getStockQuantity());
        
        productMapper.delete(product.getId());
        session.commit();
    }
    
    @Test
    void testFindAll() {
        List<Product> initialProducts = productMapper.findAll();
        int initialCount = initialProducts.size();
        
        Product product1 = new Product("Product 1", "Description 1", new BigDecimal("100.00"), 10);
        Product product2 = new Product("Product 2", "Description 2", new BigDecimal("200.00"), 20);
        
        productMapper.insert(product1);
        productMapper.insert(product2);
        session.commit();
        
        List<Product> allProducts = productMapper.findAll();
        assertEquals(initialCount + 2, allProducts.size(), "Should have two more products");
        
        productMapper.delete(product1.getId());
        productMapper.delete(product2.getId());
        session.commit();
    }
    
    @Test
    void testUpdate() {
        Product product = new Product("Original Product", "Original description", 
                                    new BigDecimal("99.99"), 10);
        productMapper.insert(product);
        session.commit();
        
        product.setName("Updated Product");
        product.setDescription("Updated description");
        product.setPrice(new BigDecimal("149.99"));
        product.setStockQuantity(15);
        
        productMapper.update(product);
        session.commit();
        
        Product updatedProduct = productMapper.findById(product.getId());
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals("Updated description", updatedProduct.getDescription());
        assertEquals(new BigDecimal("149.99"), updatedProduct.getPrice());
        assertEquals(Integer.valueOf(15), updatedProduct.getStockQuantity());
        
        productMapper.delete(product.getId());
        session.commit();
    }
    
    @Test
    void testDelete() {
        Product product = new Product("To Delete", "Will be deleted", 
                                    new BigDecimal("50.00"), 1);
        productMapper.insert(product);
        session.commit();
        
        Long productId = product.getId();
        assertNotNull(productMapper.findById(productId), "Product should exist before delete");
        
        productMapper.delete(productId);
        session.commit();
        
        assertNull(productMapper.findById(productId), "Product should not exist after delete");
    }
    
    @Test
    void testPricePrecision() {
        Product product = new Product("Precision Test", "Testing decimal precision", 
                                    new BigDecimal("123.456"), 1);
        productMapper.insert(product);
        session.commit();
        
        Product foundProduct = productMapper.findById(product.getId());
        assertEquals(new BigDecimal("123.46"), foundProduct.getPrice(), 
                    "Price should be rounded to 2 decimal places");
        
        productMapper.delete(product.getId());
        session.commit();
    }
    
    void cleanup() {
        if (session != null) {
            session.close();
        }
    }
}