package com.oliver.eshop.h2.product;

import com.oliver.eshop.domain.Product;
import com.oliver.eshop.h2.product.mapping.DatabaseProductMapper;
import com.oliver.eshop.h2.product.repository.ProductJpaRepository;
import com.oliver.eshop.service.product.port.ReadProductsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Component
@AllArgsConstructor
public class H2ReadProductRepository implements ReadProductsRepository {

    private final ProductJpaRepository productJpaRepository;
    private final DatabaseProductMapper databaseProductMapper;

    @Override
    public Product getProductById(UUID id) {
        return databaseProductMapper.toDomainModel(productJpaRepository.findById(id).orElse(null));
    }

    @Override
    public List<Product> getAllProducts() {
        return productJpaRepository.findAll().stream().map(databaseProductMapper::toDomainModel).collect(toList());
    }

    @Override
    public List<Product> getAllProductsByIds(List<UUID> ids) {
        return productJpaRepository.findAllByIdIn(ids).stream().map(databaseProductMapper::toDomainModel).collect(toList());
    }
}


