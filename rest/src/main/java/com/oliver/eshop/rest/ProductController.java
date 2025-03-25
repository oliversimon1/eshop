package com.oliver.eshop.rest;

import com.oliver.eshop.domain.Product;
import com.oliver.eshop.rest.api.ProductsApi;
import com.oliver.eshop.rest.mapping.RestProductMapper;
import com.oliver.eshop.rest.model.CreateProductRequest;
import com.oliver.eshop.rest.model.ProductModel;
import com.oliver.eshop.rest.model.ProductsResponse;
import com.oliver.eshop.rest.model.UpdateProductRequest;
import com.oliver.eshop.rest.model.UpdateStockRequest;
import com.oliver.eshop.service.product.QueryProductsService;
import com.oliver.eshop.service.product.WriteProductsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class ProductController implements ProductsApi {

    private final QueryProductsService queryProductsService;
    private final WriteProductsService writeProductsService;
    private final RestProductMapper restProductMapper;

    @Override
    public ResponseEntity<ProductsResponse> getProducts() {
        return ResponseEntity.ok(
                new ProductsResponse(
                        queryProductsService.getProducts().stream()
                                .map(restProductMapper::toRestModel)
                                .collect(Collectors.toList())
                )
        );
    }

    @Override
    public ResponseEntity<ProductModel> createProduct(CreateProductRequest createProductRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                restProductMapper.toRestModel(
                        writeProductsService.createProduct(
                                new Product(
                                        createProductRequest.getName(),
                                        createProductRequest.getPrice(),
                                        createProductRequest.getStock()
                                )
                        )
                )
        );
    }

    @Override
    public ResponseEntity<Void> deleteProduct(UUID productId) {
        writeProductsService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ProductModel> updateProduct(UUID productId, UpdateProductRequest updateProductRequest) {
        return ResponseEntity.ok(
                restProductMapper.toRestModel(
                        writeProductsService
                                .updateProduct(productId, updateProductRequest.getName(), updateProductRequest.getPrice())
                )
        );
    }

    @Override
    public ResponseEntity<ProductModel> updateProductStock(UUID productId, UpdateStockRequest updateStockRequest) {
        return ResponseEntity.ok(
                restProductMapper.toRestModel(
                        writeProductsService.patchProductStock(productId, updateStockRequest.getStock())
                )
        );
    }
}
