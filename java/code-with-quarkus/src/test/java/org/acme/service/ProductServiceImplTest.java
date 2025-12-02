package org.acme.service;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.domain.Product;
import org.acme.repository.ProductRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class ProductServiceImplTest {

    @Inject
    ProductService productService;

    @InjectMock
    ProductRepository productRepository;

    @Test
    void shouldCreateProductSuccessfully() {
        Product product = new Product("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 10);
        Product savedProduct = new Product("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 10);
        savedProduct.setId(1L);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.createProduct(product);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenCreateProductWithEmptyName() {
        Product product = new Product("", "Description", new BigDecimal("100.00"), 10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(product);
        });

        assertEquals("Product name cannot be empty", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenCreateProductWithNegativePrice() {
        Product product = new Product("Product", "Description", new BigDecimal("-100.00"), 10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(product);
        });

        assertEquals("Product price must be positive", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenCreateProductWithNegativeStock() {
        Product product = new Product("Product", "Description", new BigDecimal("100.00"), -5);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(product);
        });

        assertEquals("Product stock cannot be negative", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldGetProductById() {
        Product product = new Product("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 10);
        product.setId(1L);

        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("Laptop", result.get().getName());
        verify(productRepository, times(1)).findByIdOptional(1L);
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        when(productRepository.findByIdOptional(999L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(999L);

        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findByIdOptional(999L);
    }

    @Test
    void shouldGetAllProducts() {
        Product product1 = new Product("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 10);
        Product product2 = new Product("Mouse", "Gaming mouse", new BigDecimal("50.00"), 100);
        product1.setId(1L);
        product2.setId(2L);

        when(productRepository.listAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> results = productService.getAllProducts();

        assertEquals(2, results.size());
        verify(productRepository, times(1)).listAll();
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        Product existingProduct = new Product("Laptop", "Old description", new BigDecimal("1500.00"), 10);
        existingProduct.setId(1L);

        Product updatedData = new Product("Laptop Updated", "New description", new BigDecimal("1600.00"), 15);

        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Product result = productService.updateProduct(1L, updatedData);

        assertEquals("Laptop Updated", result.getName());
        assertEquals("New description", result.getDescription());
        assertEquals(new BigDecimal("1600.00"), result.getPrice());
        assertEquals(15, result.getStock());
        verify(productRepository, times(1)).findByIdOptional(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdateNonExistingProduct() {
        Product updatedData = new Product("Laptop", "Description", new BigDecimal("1500.00"), 10);

        when(productRepository.findByIdOptional(999L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(999L, updatedData);
        });

        assertEquals("Product not found with id: 999", exception.getMessage());
        verify(productRepository, times(1)).findByIdOptional(999L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        Product product = new Product("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 10);
        product.setId(1L);

        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(product));
        when(productRepository.deleteById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).findByIdOptional(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeleteNonExistingProduct() {
        when(productRepository.findByIdOptional(999L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.deleteProduct(999L);
        });

        assertEquals("Product not found with id: 999", exception.getMessage());
        verify(productRepository, times(1)).findByIdOptional(999L);
        verify(productRepository, never()).deleteById(anyLong());
    }
}
