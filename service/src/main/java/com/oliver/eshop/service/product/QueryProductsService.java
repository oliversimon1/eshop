package com.oliver.eshop.service.product;

import com.oliver.eshop.domain.Product;
import com.oliver.eshop.service.product.port.ReadProductsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QueryProductsService {

    private final ReadProductsRepository readProductsRepository;

    public List<Product> getProducts() {
        return readProductsRepository.getAllProducts();
    }
}
