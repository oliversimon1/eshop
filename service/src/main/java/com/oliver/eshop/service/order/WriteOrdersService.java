package com.oliver.eshop.service.order;

import com.oliver.eshop.domain.Order;
import com.oliver.eshop.domain.OrderItem;
import com.oliver.eshop.domain.Product;
import com.oliver.eshop.domain.command.CreateOrderCommand;
import com.oliver.eshop.domain.command.OrderItemCommand;
import com.oliver.eshop.service.order.exception.OrderNotFoundException;
import com.oliver.eshop.service.order.port.ReadOrderRepository;
import com.oliver.eshop.service.order.port.WriteOrderRepository;
import com.oliver.eshop.service.product.exception.ProductNotFoundException;
import com.oliver.eshop.service.product.port.ReadProductsRepository;
import com.oliver.eshop.service.product.port.WriteProductsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class WriteOrdersService {

    private final WriteOrderRepository writeOrderRepository;
    private final ReadOrderRepository readOrderRepository;
    private final ReadProductsRepository readProductsRepository;
    private final WriteProductsRepository writeProductsRepository;

    @Transactional
    public Order createOrder(CreateOrderCommand orderCommand) {
        List<Product> orderProducts = readProductsRepository
                .getAllProductsByIds(orderCommand.getOrderItems().stream().map(OrderItemCommand::getProductId).toList());

        List<OrderItem> orderItems = orderCommand.getOrderItems().stream().map(orderItem -> {
            Product product = orderProducts.stream()
                    .filter(p -> p.getId().equals(orderItem.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new ProductNotFoundException(orderItem.getProductId()));
            return new OrderItem(product, orderItem.getQuantity(), product.getName(), product.getPrice());
        }).toList();

        orderItems.forEach(OrderItem::updateProductStock);

        writeProductsRepository.saveAll(orderProducts);

        return writeOrderRepository.save(new Order(orderItems));
    }

    @Transactional
    public Order payOrder(UUID orderId) {
        Order order = readOrderRepository.getOrderById(orderId);

        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        order.finishOrder();

        return writeOrderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Order order) {
        List<Product> orderedProducts = order.getOrderItems().stream().map(OrderItem::getProduct).toList();

        order.cancelOrder();

        writeProductsRepository.saveAll(orderedProducts);
        writeOrderRepository.save(order);
    }
}
