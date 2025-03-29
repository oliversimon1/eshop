package com.oliver.eshop.domain;

import com.oliver.eshop.domain.exception.ValidationException;
import com.oliver.eshop.domain.utils.NullUtils;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class Product {
    private UUID id;
    private String name;
    private Double price;
    private int stock;

    private Integer version;
    private OffsetDateTime createdAt;
    private OffsetDateTime modifiedAt;

    public Product(String name, Double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        validate();
    }

    public void updateProduct(String name, Double price) {
        NullUtils.ifNotNull(name, newName -> this.name = newName);
        NullUtils.ifNotNull(price, newPrice -> this.price = newPrice);
        validate();
    }

    public void updateStock(int stock) {
        this.stock += stock;
        validate();
    }

    private void validate() {
        StringBuilder errorMessage = new StringBuilder();

        if (name == null) {
            errorMessage.append("Name cannot be null. ");
        }

        if (price == null) {
            errorMessage.append("Price cannot be null. ");
        } else if (price <= 0) {
            errorMessage.append("Price must be greater than 0. ");
        }

        if (stock < 0) {
            errorMessage.append("Not enough stock. ");
        }

        if (!errorMessage.isEmpty()) {
            throw new ValidationException(errorMessage.toString());
        }
    }

}
