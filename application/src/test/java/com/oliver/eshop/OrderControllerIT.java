package com.oliver.eshop;

import com.oliver.eshop.domain.OrderStatus;
import com.oliver.eshop.h2.order.entity.OrderEntity;
import com.oliver.eshop.h2.order.entity.OrderItemEntity;
import com.oliver.eshop.h2.product.entity.ProductEntity;
import com.oliver.eshop.rest.model.CreateOrderRequest;
import com.oliver.eshop.rest.model.OrderItemRequest;
import com.oliver.eshop.rest.model.OrderModel;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OrderControllerIT extends AbstractIT {

    @Test
    void createOrder_ProductNotFound() {
        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setProductId(UUID.randomUUID());
        orderItemRequest.setQuantity(20);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setOrderItems(List.of(orderItemRequest));

        ResponseEntity<Void> response = restTemplate.postForEntity("/orders", createOrderRequest, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createOrder_ProductNotEnoughStock() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName("Name");
        productEntity.setPrice(10.1);
        productEntity.setStock(10);
        productEntity = productJpaRepository.save(productEntity);

        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setProductId(productEntity.getId());
        orderItemRequest.setQuantity(20);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setOrderItems(List.of(orderItemRequest));

        ResponseEntity<Void> response = restTemplate.postForEntity("/orders", createOrderRequest, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createOrder_Success() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName("Name");
        productEntity.setPrice(10.0);
        productEntity.setStock(20);
        productEntity = productJpaRepository.save(productEntity);

        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setProductId(productEntity.getId());
        orderItemRequest.setQuantity(20);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setOrderItems(List.of(orderItemRequest));

        ResponseEntity<OrderModel> response = restTemplate.postForEntity("/orders", createOrderRequest, OrderModel.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals(200.0, response.getBody().getPrice());
    }

    @Test
    void payForOrder_OrderNotFound() {
        ResponseEntity<Void> response = restTemplate.postForEntity("/orders/{orderId}/payment", null, Void.class, UUID.randomUUID());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void payForOrder_Success() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName("Name");
        productEntity.setPrice(10.0);
        productEntity.setStock(20);
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

        ResponseEntity<OrderModel> response = restTemplate.postForEntity("/orders/{orderId}/payment", null,
                OrderModel.class, orderEntity.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());

        OrderEntity updatedOrderEntity = orderJpaRepository.findById(orderEntity.getId()).get();
        assertEquals(OrderStatus.FINISHED, updatedOrderEntity.getStatus());
        assertNull(updatedOrderEntity.getOrderItems().getFirst().getProduct());
    }
}
