package com.oliver.eshop;

import com.oliver.eshop.domain.OrderStatus;
import com.oliver.eshop.h2.order.entity.OrderEntity;
import com.oliver.eshop.h2.order.entity.OrderItemEntity;
import com.oliver.eshop.h2.product.entity.ProductEntity;
import com.oliver.eshop.rest.model.CreateProductRequest;
import com.oliver.eshop.rest.model.ProductModel;
import com.oliver.eshop.rest.model.ProductsResponse;
import com.oliver.eshop.rest.model.UpdateProductRequest;
import com.oliver.eshop.rest.model.UpdateStockRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductControllerIT extends AbstractIT {

    @Test
    void getProducts_Success() {
        String url = "/products";
        ResponseEntity<ProductsResponse> response = restTemplate.getForEntity(url, ProductsResponse.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void createProduct_Success() {
        String url = "/products";
        ResponseEntity<ProductModel> response = restTemplate.postForEntity(url,
                new CreateProductRequest("Name", 10.1),
                ProductModel.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void updateProduct_ProductNotFound() {
        UpdateProductRequest updateProductRequest = new UpdateProductRequest();
        updateProductRequest.setName("New Name");
        updateProductRequest.setPrice(20.2);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/products/{productId}",
                HttpMethod.PUT,
                new HttpEntity<>(updateProductRequest),
                Void.class,
                UUID.randomUUID());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateProduct_Success() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName("Name");
        productEntity.setPrice(10.1);
        productEntity.setStock(10);
        productEntity = productJpaRepository.save(productEntity);

        UpdateProductRequest updateProductRequest = new UpdateProductRequest();
        updateProductRequest.setName("New Name");
        updateProductRequest.setPrice(20.2);

        ResponseEntity<ProductModel> response = restTemplate.exchange(
                "/products/{productId}",
                HttpMethod.PUT,
                new HttpEntity<>(updateProductRequest),
                ProductModel.class,
                productEntity.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New Name", response.getBody().getName());
        assertEquals(20.2, response.getBody().getPrice());
    }

    @Test
    void patchProductStock_ProductNotFound() {
        UpdateStockRequest updateStockRequest = new UpdateStockRequest();
        updateStockRequest.setStock(10);

        ResponseEntity<ProductModel> response = restTemplate.exchange(
                "/products/{productId}/stock",
                HttpMethod.PATCH,
                new HttpEntity<>(updateStockRequest),
                ProductModel.class,
                UUID.randomUUID());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void patchProductStock_Success() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName("Name");
        productEntity.setPrice(10.1);
        productEntity.setStock(10);
        productEntity = productJpaRepository.save(productEntity);

        UpdateStockRequest updateStockRequest = new UpdateStockRequest();
        updateStockRequest.setStock(10);

        ResponseEntity<ProductModel> response = restTemplate.exchange(
                "/products/{productId}/stock",
                HttpMethod.PATCH,
                new HttpEntity<>(updateStockRequest),
                ProductModel.class,
                productEntity.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(20, response.getBody().getStock());
    }

    @Test
    void deleteProduct_ProductInActiveOrderFailsToDelete() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName("Name");
        productEntity.setPrice(10.1);
        productEntity.setStock(10);
        productEntity = productJpaRepository.save(productEntity);

        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setQuantity(1);
        orderItem.setProduct(productEntity);
        orderItem.setProductPrice(10.1);
        orderItem.setProductName("Name");

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setPrice(10.1);
        orderEntity.setStatus(OrderStatus.AWAITING_PAYMENT);
        orderEntity.setOrderItems(List.of(orderItem));

        orderItem.setOrder(orderEntity);

        orderJpaRepository.save(orderEntity);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/products/{productId}",
                HttpMethod.DELETE,
                null,
                Void.class,
                productEntity.getId());

        assertEquals(HttpStatus.BAD_REQUEST, deleteResponse.getStatusCode());
    }

    @Test
    void deleteProduct_Success() {
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/products/{productId}",
                HttpMethod.DELETE,
                null,
                Void.class,
                UUID.randomUUID());

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
    }

}
