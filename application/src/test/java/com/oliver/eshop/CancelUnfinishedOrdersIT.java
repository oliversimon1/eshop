package com.oliver.eshop;

import com.oliver.eshop.domain.OrderStatus;
import com.oliver.eshop.h2.order.entity.OrderEntity;
import com.oliver.eshop.h2.order.entity.OrderItemEntity;
import com.oliver.eshop.h2.product.entity.ProductEntity;
import com.oliver.eshop.service.order.WriteOrdersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CancelUnfinishedOrdersIT extends AbstractIT {

    @Autowired
    private WriteOrdersService writeOrdersService;

    @Test
    void cancelUnfinishedOrders() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName("Name");
        productEntity.setPrice(10.0);
        productEntity.setStock(18);
        productEntity = productJpaRepository.save(productEntity);

        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setProduct(productEntity);
        orderItemEntity.setQuantity(2);
        orderItemEntity.setProductPrice(10.0);
        orderItemEntity.setProductName("Name");

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setPrice(20.0);
        orderEntity.setStatus(OrderStatus.AWAITING_PAYMENT);
        orderEntity.setOrderItems(List.of(orderItemEntity));

        orderItemEntity.setOrder(orderEntity);
        orderEntity = orderJpaRepository.save(orderEntity);

        writeOrdersService.scheduleCancelUnfinishedOrders();

        orderEntity = orderJpaRepository.findById(orderEntity.getId()).get();
        assertEquals(OrderStatus.CANCELED, orderEntity.getStatus());
        assertNull(orderEntity.getOrderItems().getFirst().getProduct());

        productEntity = productJpaRepository.findById(productEntity.getId()).get();
        assertEquals(20, productEntity.getStock());
    }
}
