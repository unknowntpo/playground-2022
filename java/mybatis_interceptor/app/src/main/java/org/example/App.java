package org.example;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.mapper.UserMapper;
import org.example.mapper.ProductMapper;
import org.example.model.User;
import org.example.model.Product;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

public class App {
    
    public static void main(String[] args) {
        try {
            System.out.println("Starting MyBatis Interceptor Example with Transaction Testing...");
            
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            
            // Test basic operations with logging
            testTransactionBehavior(sqlSessionFactory);
            
            System.out.println("Example completed. Check the change_event table to see audit logs.");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void testTransactionBehavior(SqlSessionFactory sqlSessionFactory) {
        System.out.println("\n=== Testing Transaction Behavior ===");
        
        // Test 1: Normal commit
        System.out.println("\n--- Test 1: Normal Transaction Commit ---");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            
            System.out.println("Inserting user...");
            User user = new User("test_tx_user", "tx@example.com", "Transaction", "Test");
            userMapper.insert(user);
            
            System.out.println("Committing transaction...");
            session.commit();
            System.out.println("Transaction committed successfully");
            
            // Clean up
            userMapper.delete(user.getId());
            session.commit();
            
        } catch (Exception e) {
            System.err.println("Error in commit test: " + e.getMessage());
        }
        
        // Test 2: Rollback behavior
        System.out.println("\n--- Test 2: Transaction Rollback ---");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            
            System.out.println("Inserting user (will be rolled back)...");
            User user = new User("test_rollback_user", "rollback@example.com", "Rollback", "Test");
            userMapper.insert(user);
            System.out.println("User inserted with ID: " + user.getId());
            
            System.out.println("Rolling back transaction...");
            session.rollback();
            System.out.println("Transaction rolled back");
            
        } catch (Exception e) {
            System.err.println("Error in rollback test: " + e.getMessage());
        }
    }
    
    private static void testUserOperations(SqlSessionFactory sqlSessionFactory) {
        System.out.println("\n=== Testing User Operations ===");
        
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            
            System.out.println("1. Inserting new user...");
            User newUser = new User("test_user", "test@example.com", "Test", "User");
            userMapper.insert(newUser);
            session.commit();
            System.out.println("Inserted user: " + newUser);
            
            System.out.println("2. Updating user...");
            newUser.setEmail("updated@example.com");
            userMapper.update(newUser);
            session.commit();
            System.out.println("Updated user: " + newUser);
            
            System.out.println("3. Listing all users:");
            List<User> users = userMapper.findAll();
            users.forEach(System.out::println);
            
            System.out.println("4. Deleting user...");
            userMapper.delete(newUser.getId());
            session.commit();
            System.out.println("Deleted user with ID: " + newUser.getId());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void testProductOperations(SqlSessionFactory sqlSessionFactory) {
        System.out.println("\n=== Testing Product Operations ===");
        
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ProductMapper productMapper = session.getMapper(ProductMapper.class);
            
            System.out.println("1. Inserting new product...");
            Product newProduct = new Product("Test Product", "A test product", new BigDecimal("99.99"), 5);
            productMapper.insert(newProduct);
            session.commit();
            System.out.println("Inserted product: " + newProduct);
            
            System.out.println("2. Updating product...");
            newProduct.setPrice(new BigDecimal("89.99"));
            newProduct.setStockQuantity(10);
            productMapper.update(newProduct);
            session.commit();
            System.out.println("Updated product: " + newProduct);
            
            System.out.println("3. Listing all products:");
            List<Product> products = productMapper.findAll();
            products.forEach(System.out::println);
            
            System.out.println("4. Deleting product...");
            productMapper.delete(newProduct.getId());
            session.commit();
            System.out.println("Deleted product with ID: " + newProduct.getId());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
