package com.oliver.eshop.domain;

import com.oliver.eshop.domain.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class Order {
    private UUID id;
    private OrderStatus status;
    private List<OrderItem> orderItems;
    private Double price;

    private Integer version;
    private OffsetDateTime createdAt;
    private OffsetDateTime modifiedAt;

    public Order(List<OrderItem> orderItems) {
        status = OrderStatus.AWAITING_PAYMENT;
        this.orderItems = orderItems;
        price = orderItems.stream()
                .map(orderItem ->
                        orderItem.getQuantity() * orderItem.getProduct().getPrice())
                .reduce(0.0, Double::sum);
        validate();
    }

    public void finishOrder() {
        if (status != OrderStatus.AWAITING_PAYMENT) {
            throw new ValidationException("Order must be awaiting payment to be finished.");
        }

        status = OrderStatus.FINISHED;
        orderItems.forEach(OrderItem::dereferenceProduct);
    }

    public void cancelOrder() {
        if (status != OrderStatus.AWAITING_PAYMENT) {
            throw new ValidationException("Order must be awaiting payment to be cancelled.");
        }

        status = OrderStatus.CANCELED;
        orderItems.forEach(OrderItem::revertProductStock);
        orderItems.forEach(OrderItem::dereferenceProduct);
    }

    public void validate() {
        if (orderItems.isEmpty()) {
            throw new ValidationException("Order must have at least one item.");
        }
    }

}
