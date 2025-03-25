package com.oliver.eshop.h2.order.mapping;

import com.oliver.eshop.domain.Order;
import com.oliver.eshop.domain.OrderItem;
import com.oliver.eshop.domain.Product;
import com.oliver.eshop.h2.order.entity.OrderEntity;
import com.oliver.eshop.h2.order.entity.OrderItemEntity;
import com.oliver.eshop.h2.product.entity.ProductEntity;
import com.oliver.eshop.h2.product.mapping.DatabaseProductMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DatabaseProductMapper.class})
public interface DatabaseOrderMapper {

    OrderEntity toEntity(Order order);

    @AfterMapping
    default void linkOrderItems(@MappingTarget OrderEntity orderEntity) {
        if (orderEntity.getOrderItems() != null) {
            orderEntity.getOrderItems().forEach(item -> item.setOrder(orderEntity));
        }
    }

    default Order toDomainModel(OrderEntity orderEntity) {
        if (orderEntity == null) return null;

        return new Order(orderEntity.getId(), orderEntity.getStatus(),
                toDomainModel(orderEntity.getOrderItems()),
                orderEntity.getPrice(),
                orderEntity.getVersion(),
                orderEntity.getCreatedAt(),
                orderEntity.getModifiedAt());
    }

    default OrderItem toDomainModel(OrderItemEntity orderItemEntity) {
        if (orderItemEntity == null) return null;

        return new OrderItem(toDomainModel(orderItemEntity.getProduct()),
                orderItemEntity.getQuantity(),
                orderItemEntity.getProductName(),
                orderItemEntity.getProductPrice());
    }

    List<OrderItem> toDomainModel(List<OrderItemEntity> orderItemEntity);

    default Product toDomainModel(ProductEntity productEntity) {
        if (productEntity == null) {
            return null;
        }

        return new Product(productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getStock());
    }
}
