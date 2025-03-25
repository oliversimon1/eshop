package com.oliver.eshop.domain.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class OrderItemCommand {
    private UUID productId;
    private int quantity;
}
