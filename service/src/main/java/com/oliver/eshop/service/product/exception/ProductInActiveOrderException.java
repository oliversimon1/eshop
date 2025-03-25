package com.oliver.eshop.service.product.exception;

import java.util.UUID;

public class ProductInActiveOrderException extends RuntimeException {
    public ProductInActiveOrderException(UUID productId) {
        super("Product with id " + productId + " is in active order and cannot be deleted.");
    }
}
