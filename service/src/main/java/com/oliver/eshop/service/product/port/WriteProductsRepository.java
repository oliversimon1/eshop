package com.oliver.eshop.service.product.port;

import com.oliver.eshop.domain.Product;

import java.util.List;
import java.util.UUID;

public interface WriteProductsRepository {
    Product save(Product product);
    void saveAll(List<Product> products);
    void delete(UUID productId);
}
