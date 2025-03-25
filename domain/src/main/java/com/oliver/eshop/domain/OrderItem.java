package com.oliver.eshop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderItem {
    private Product product;
    private int quantity;
    private String productName;
    private Double productPrice;

    public OrderItem(Product product, int quantity, String productName, Double productPrice) {
        this.product = product;
        this.quantity = quantity;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public void updateProductStock() {
        product.updateStock(-this.quantity);
    }

    public void dereferenceProduct() {
        product = null;
    }

    public void revertProductStock() {
        product.updateStock(this.quantity);
    }
}
