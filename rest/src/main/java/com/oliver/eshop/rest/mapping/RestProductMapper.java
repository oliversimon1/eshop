package com.oliver.eshop.rest.mapping;

import com.oliver.eshop.domain.Product;
import com.oliver.eshop.rest.model.ProductModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestProductMapper {

    ProductModel toRestModel(Product product);

}
