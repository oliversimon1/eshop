package com.oliver.eshop.service.order.port;

import com.oliver.eshop.domain.Order;

public interface WriteOrderRepository {

    Order save(Order order);
}
