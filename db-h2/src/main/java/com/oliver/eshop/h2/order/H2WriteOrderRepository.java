package com.oliver.eshop.h2.order;

import com.oliver.eshop.domain.Order;
import com.oliver.eshop.h2.order.mapping.DatabaseOrderMapper;
import com.oliver.eshop.h2.order.repository.OrderJpaRepository;
import com.oliver.eshop.service.order.port.WriteOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class H2WriteOrderRepository implements WriteOrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final DatabaseOrderMapper databaseOrderMapper;

    @Override
    public Order save(Order order) {

        return databaseOrderMapper.toDomainModel(
                orderJpaRepository.save(
                        databaseOrderMapper.toEntity(order)
                )
        );
    }
}
