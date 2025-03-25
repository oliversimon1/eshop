package com.oliver.eshop.rest.mapping;

import com.oliver.eshop.domain.Order;
import com.oliver.eshop.domain.OrderItem;
import com.oliver.eshop.domain.command.CreateOrderCommand;
import com.oliver.eshop.domain.command.OrderItemCommand;
import com.oliver.eshop.rest.model.CreateOrderRequest;
import com.oliver.eshop.rest.model.OrderItemModel;
import com.oliver.eshop.rest.model.OrderItemRequest;
import com.oliver.eshop.rest.model.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestOrderMapper {

    CreateOrderCommand toCreateOrderCommand(CreateOrderRequest createOrderRequest);

    default OrderItemCommand toDomainModel(OrderItemRequest orderItemRequest) {
        return new OrderItemCommand(
                orderItemRequest.getProductId(),
                orderItemRequest.getQuantity()
        );
    }

    OrderModel toOrderModel(Order order);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "productPrice", target = "price")
    OrderItemModel toOrderItemModel(OrderItem orderItem);
}
