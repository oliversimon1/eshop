package com.oliver.eshop.service.product.port;

import com.oliver.eshop.domain.Product;

import java.util.List;
import java.util.UUID;

public interface ReadProductsRepository {

    Product getProductById(UUID id);
    List<Product> getAllProducts();
    List<Product> getAllProductsByIds(List<UUID> ids);
}
