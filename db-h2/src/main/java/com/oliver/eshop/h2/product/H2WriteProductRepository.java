package com.oliver.eshop.h2.product;

import com.oliver.eshop.domain.Product;
import com.oliver.eshop.h2.product.mapping.DatabaseProductMapper;
import com.oliver.eshop.h2.product.repository.ProductJpaRepository;
import com.oliver.eshop.service.product.exception.ProductInActiveOrderException;
import com.oliver.eshop.service.product.port.WriteProductsRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class H2WriteProductRepository implements WriteProductsRepository {

    private final ProductJpaRepository productJpaRepository;
    private final DatabaseProductMapper databaseProductMapper;

    @Override
    public Product save(Product product) {
        return databaseProductMapper.toDomainModel(
                productJpaRepository.save(
                        databaseProductMapper.toEntity(product)
                )
        );
    }

    @Override
    public void saveAll(List<Product> products) {
        productJpaRepository.saveAll(
                products.stream().map(databaseProductMapper::toEntity).toList());
    }

    @Override
    public void delete(UUID productId) {
        try {
            productJpaRepository.deleteById(productId);
        } catch (DataIntegrityViolationException e) {
            throw new ProductInActiveOrderException(productId);
        }
    }
}
