package com.oliver.eshop.service.product;


import com.oliver.eshop.domain.Product;
import com.oliver.eshop.domain.exception.ValidationException;
import com.oliver.eshop.service.product.exception.ProductNotFoundException;
import com.oliver.eshop.service.product.port.ReadProductsRepository;
import com.oliver.eshop.service.product.port.WriteProductsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WriteProductsServiceTest {

    @Mock
    private ReadProductsRepository readProductsRepository;

    @Mock
    private WriteProductsRepository writeProductsRepository;

    @InjectMocks
    private WriteProductsService writeProductsService;

    private UUID productId;
    private Product product;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        product = new Product(productId, "Product 1", 100.0, 10);
    }

    @Test
    void createProduct_shouldCreateProductSuccessfully() {
        when(writeProductsRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = writeProductsService.createProduct(product);

        assertNotNull(createdProduct);
        assertEquals(productId, createdProduct.getId());
        verify(writeProductsRepository).save(product);
    }

    @Test
    void createProduct_nullValuesThrowValidationException() {
        assertThrows(ValidationException.class, () -> new Product(null, 10.1, 1));
        assertThrows(ValidationException.class, () -> new Product("Name", null, 1));
        assertThrows(ValidationException.class, () -> new Product("Name", 10.1, -10));
    }

    @Test
    void getProduct_shouldReturnProductSuccessfully() {
        when(readProductsRepository.getProductById(productId)).thenReturn(product);

        Product foundProduct = writeProductsService.getProduct(productId);

        assertNotNull(foundProduct);
        assertEquals(productId, foundProduct.getId());
        verify(readProductsRepository).getProductById(productId);
    }

    @Test
    void getProduct_shouldThrowProductNotFoundException() {
        when(readProductsRepository.getProductById(productId)).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> writeProductsService.getProduct(productId));
    }

    @Test
    void updateProduct_shouldUpdateProductSuccessfully() {
        when(readProductsRepository.getProductById(productId)).thenReturn(product);
        when(writeProductsRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = writeProductsService.updateProduct(productId, "Updated Product", 150.0);

        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals(150.0, updatedProduct.getPrice());
        verify(readProductsRepository).getProductById(productId);
        verify(writeProductsRepository).save(product);
    }

    @Test
    void patchProductStock_shouldUpdateProductStockSuccessfully() {
        when(readProductsRepository.getProductById(productId)).thenReturn(product);
        when(writeProductsRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = writeProductsService.patchProductStock(productId, 20);

        assertNotNull(updatedProduct);
        assertEquals(30, updatedProduct.getStock());
        verify(readProductsRepository).getProductById(productId);
        verify(writeProductsRepository).save(product);
    }

    @Test
    void deleteProduct_shouldDeleteProductSuccessfully() {
        doNothing().when(writeProductsRepository).delete(productId);

        writeProductsService.deleteProduct(productId);

        verify(writeProductsRepository).delete(productId);
    }
}
