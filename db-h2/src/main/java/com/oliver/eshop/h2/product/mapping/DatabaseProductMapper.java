package com.oliver.eshop.h2.product.mapping;

import com.oliver.eshop.domain.Product;
import com.oliver.eshop.h2.product.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DatabaseProductMapper {

    ProductEntity toEntity(Product product);

    default Product toDomainModel(ProductEntity productEntity) {
        if (productEntity == null) {
            return null;
        }

        return new Product(productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getStock(),
                productEntity.getVersion(),
                productEntity.getCreatedAt(),
                productEntity.getModifiedAt());
    }
}
