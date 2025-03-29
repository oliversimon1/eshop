package com.oliver.eshop.rest;

import com.oliver.eshop.rest.api.OrdersApi;
import com.oliver.eshop.rest.mapping.RestOrderMapper;
import com.oliver.eshop.rest.model.CreateOrderRequest;
import com.oliver.eshop.rest.model.OrderModel;
import com.oliver.eshop.service.exception.OptimisticLockingException;
import com.oliver.eshop.service.order.WriteOrdersService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class OrderController implements OrdersApi {

    private final WriteOrdersService writeOrdersService;
    private final RestOrderMapper restOrderMapper;

    @Override
    @Retryable(retryFor = OptimisticLockingException.class)
    public ResponseEntity<OrderModel> createOrder(CreateOrderRequest createOrderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                restOrderMapper.toOrderModel(
                        writeOrdersService.createOrder(
                                restOrderMapper.toCreateOrderCommand(createOrderRequest)
                        )
                )
        );
    }

    @Override
    @Retryable(retryFor = OptimisticLockingException.class)
    public ResponseEntity<OrderModel> processPayment(UUID orderId) {
        return ResponseEntity.ok(
                restOrderMapper.toOrderModel(
                        writeOrdersService.payOrder(orderId)
                )
        );
    }
}
