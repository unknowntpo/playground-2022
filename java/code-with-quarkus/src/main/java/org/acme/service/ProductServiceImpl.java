package org.acme.service;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.Product;
import org.acme.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {

    @Inject
    ProductRepository productRepository;

    @Override
    @Transactional
    @CacheInvalidateAll(cacheName = "products")
    public Product createProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (product.getPrice() == null || product.getPrice().signum() <= 0) {
            throw new IllegalArgumentException("Product price must be positive");
        }
        if (product.getStock() == null || product.getStock() < 0) {
            throw new IllegalArgumentException("Product stock cannot be negative");
        }
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findByIdOptional(id);
    }

    @Override
    @CacheResult(cacheName = "products")
    public List<Product> getAllProducts() {
        return productRepository.listAll();
    }

    @Override
    @Transactional
    @CacheInvalidateAll(cacheName = "products")
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findByIdOptional(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        if (product.getName() != null && !product.getName().trim().isEmpty()) {
            existingProduct.setName(product.getName());
        }
        if (product.getDescription() != null) {
            existingProduct.setDescription(product.getDescription());
        }
        if (product.getPrice() != null && product.getPrice().signum() > 0) {
            existingProduct.setPrice(product.getPrice());
        }
        if (product.getStock() != null && product.getStock() >= 0) {
            existingProduct.updateStock(product.getStock());
        }

        return productRepository.save(existingProduct);
    }

    @Override
    @Transactional
    @CacheInvalidateAll(cacheName = "products")
    public void deleteProduct(Long id) {
        if (!productRepository.findByIdOptional(id).isPresent()) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
