package com.oliver.eshop.domain.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreateOrderCommand {
    private List<OrderItemCommand> orderItems;
}
