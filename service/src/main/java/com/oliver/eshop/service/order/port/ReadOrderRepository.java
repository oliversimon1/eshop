package com.oliver.eshop.service.order.port;

import com.oliver.eshop.domain.Order;
import com.oliver.eshop.domain.OrderStatus;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface ReadOrderRepository {
    Order getOrderById(UUID id);
    List<Order> getAllOrdersByStatusAndCreatedAtBefore(OrderStatus status, OffsetDateTime createdAt);
}
