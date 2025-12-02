package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.acme.domain.Product;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    @Transactional
    public Product save(Product product) {
        if (product.getId() == null) {
            persist(product);
        } else {
            product = getEntityManager().merge(product);
        }
        return product;
    }
}
