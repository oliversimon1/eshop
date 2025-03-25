package com.oliver.eshop.service.product;

import com.oliver.eshop.domain.Product;
import com.oliver.eshop.service.product.exception.ProductNotFoundException;
import com.oliver.eshop.service.product.port.ReadProductsRepository;
import com.oliver.eshop.service.product.port.WriteProductsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class WriteProductsService {

    private final ReadProductsRepository readProductsRepository;
    private final WriteProductsRepository writeProductsRepository;

    public Product createProduct(Product product) {
        return writeProductsRepository.save(product);
    }

    public Product getProduct(UUID productId) {
        Product product = readProductsRepository.getProductById(productId);

        if (product == null) { throw new ProductNotFoundException(productId); }

        return product;
    }

    public Product updateProduct(UUID productId, String name, Double price) {
        Product productToUpdate = getProduct(productId);

        productToUpdate.updateProduct(name, price);

        return writeProductsRepository.save(productToUpdate);
    }

    public Product patchProductStock(UUID productId, int stock) {
        Product productToUpdate = getProduct(productId);

        productToUpdate.updateStock(stock);

        return writeProductsRepository.save(productToUpdate);
    }

    public void deleteProduct(UUID productId) {
        writeProductsRepository.delete(productId);
    }
}
