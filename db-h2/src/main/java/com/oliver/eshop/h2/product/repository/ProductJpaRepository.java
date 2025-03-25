package com.oliver.eshop.h2.product.repository;

import com.oliver.eshop.h2.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {
    List<ProductEntity> findAllByIdIn(List<UUID> ids);

    List<UUID> id(UUID id);
}
