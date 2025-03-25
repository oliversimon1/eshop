package com.oliver.eshop.h2.order.repository;

import com.oliver.eshop.domain.OrderStatus;
import com.oliver.eshop.h2.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {

    List<OrderEntity> findAllByStatusAndCreatedAtBefore(OrderStatus status, OffsetDateTime createdAt);
}
