package org.acme.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldCreateProductWithValidData() {
        Product product = new Product("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 10);

        assertEquals("Laptop", product.getName());
        assertEquals("Gaming laptop", product.getDescription());
        assertEquals(new BigDecimal("1500.00"), product.getPrice());
        assertEquals(10, product.getStock());
    }

    @Test
    void shouldUpdateStock() {
        Product product = new Product("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 10);

        product.updateStock(20);

        assertEquals(20, product.getStock());
    }

    @Test
    void shouldThrowExceptionWhenUpdateStockWithNegative() {
        Product product = new Product("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            product.updateStock(-5);
        });

        assertEquals("Stock cannot be negative", exception.getMessage());
    }

    @Test
    void shouldDecreaseStock() {
        Product product = new Product("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 10);

        product.decreaseStock(3);

        assertEquals(7, product.getStock());
    }

    @Test
    void shouldThrowExceptionWhenDecreaseStockWithNegativeQuantity() {
        Product product = new Product("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            product.decreaseStock(-5);
        });

        assertEquals("Quantity cannot be negative", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDecreaseStockWithInsufficientStock() {
        Product product = new Product("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 5);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            product.decreaseStock(10);
        });

        assertEquals("Insufficient stock", exception.getMessage());
    }

    @Test
    void shouldReturnTrueWhenProductIsInStock() {
        Product product = new Product("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 10);

        assertTrue(product.isInStock());
    }

    @Test
    void shouldReturnFalseWhenProductIsOutOfStock() {
        Product product = new Product("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 0);

        assertFalse(product.isInStock());
    }

    @Test
    void shouldBeEqualWhenSameId() {
        Product product1 = new Product("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 10);
        Product product2 = new Product("Desktop", "Gaming desktop", new BigDecimal("2000.00"), 5);

        product1.setId(1L);
        product2.setId(1L);

        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        Product product1 = new Product("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 10);
        Product product2 = new Product("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 10);

        product1.setId(1L);
        product2.setId(2L);

        assertNotEquals(product1, product2);
    }
}
