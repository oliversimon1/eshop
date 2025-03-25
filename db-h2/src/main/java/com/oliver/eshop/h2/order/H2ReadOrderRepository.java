package com.oliver.eshop.h2.order;

import com.oliver.eshop.domain.Order;
import com.oliver.eshop.domain.OrderStatus;
import com.oliver.eshop.h2.order.mapping.DatabaseOrderMapper;
import com.oliver.eshop.h2.order.repository.OrderJpaRepository;
import com.oliver.eshop.service.order.port.ReadOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class H2ReadOrderRepository implements ReadOrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final DatabaseOrderMapper databaseOrderMapper;

    @Override
    public Order getOrderById(UUID id) {
        return databaseOrderMapper.toDomainModel(orderJpaRepository.findById(id).orElse(null));
    }

    @Override
    public List<Order> getAllOrdersByStatusAndCreatedAtBefore(OrderStatus status, OffsetDateTime createdAt) {
        return orderJpaRepository
                .findAllByStatusAndCreatedAtBefore(status, createdAt)
                .stream()
                .map(databaseOrderMapper::toDomainModel)
                .toList();
    }
}
