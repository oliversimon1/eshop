package com.oliver.eshop.service.order;

import com.oliver.eshop.domain.Order;
import com.oliver.eshop.domain.OrderItem;
import com.oliver.eshop.domain.OrderStatus;
import com.oliver.eshop.domain.Product;
import com.oliver.eshop.domain.command.CreateOrderCommand;
import com.oliver.eshop.domain.command.OrderItemCommand;
import com.oliver.eshop.domain.exception.ValidationException;
import com.oliver.eshop.service.order.exception.OrderNotFoundException;
import com.oliver.eshop.service.order.port.ReadOrderRepository;
import com.oliver.eshop.service.order.port.WriteOrderRepository;
import com.oliver.eshop.service.product.exception.ProductNotFoundException;
import com.oliver.eshop.service.product.port.ReadProductsRepository;
import com.oliver.eshop.service.product.port.WriteProductsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WriteOrdersServiceTest {

    @Mock
    private WriteOrderRepository writeOrderRepository;

    @Mock
    private ReadOrderRepository readOrderRepository;

    @Mock
    private ReadProductsRepository readProductsRepository;

    @Mock
    private WriteProductsRepository writeProductsRepository;

    @InjectMocks
    private WriteOrdersService writeOrdersService;

    private CreateOrderCommand createOrderCommand;
    private Order order;
    private UUID orderId;
    private UUID productId;
    private Product product;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        product = new Product(productId, "Product 1", 100.0, 100, 0, OffsetDateTime.now(), OffsetDateTime.now());
        OrderItemCommand orderItemCommand = new OrderItemCommand(productId, 2);
        createOrderCommand = new CreateOrderCommand(List.of(orderItemCommand));
        OrderItem orderItem = new OrderItem(product, 2, "Product 1", 100.0);
        order = new Order(List.of(orderItem));
        orderId = UUID.randomUUID();
    }

    @Test
    void createOrder_shouldCreateOrderSuccessfully() {
        when(readProductsRepository.getAllProductsByIds(anyList())).thenReturn(List.of(product));
        when(writeOrderRepository.save(any(Order.class))).thenReturn(order);

        Order createdOrder = writeOrdersService.createOrder(createOrderCommand);

        assertNotNull(createdOrder);
        verify(writeProductsRepository).saveAll(
                List.of(new Product(productId, "Product 1", 100.0, 98,
                        product.getVersion(), product.getCreatedAt(), product.getModifiedAt()))
        );
        verify(writeOrderRepository).save(any(Order.class));
        assertEquals(200.0, createdOrder.getPrice());
        assertEquals(OrderStatus.AWAITING_PAYMENT, createdOrder.getStatus());
    }

    @Test
    void createOrder_shouldThrowProductNotFoundException() {
        when(readProductsRepository.getAllProductsByIds(anyList())).thenReturn(List.of());

        assertThrows(ProductNotFoundException.class, () -> writeOrdersService.createOrder(createOrderCommand));
    }

    @Test
    void createOrder_shouldThrowNotEnoughStock() {
        when(readProductsRepository.getAllProductsByIds(anyList())).thenReturn(
                List.of(new Product(productId, "Product 1", 100.0, 0, null, null, null))
        );

        assertThrows(ValidationException.class, () -> writeOrdersService.createOrder(createOrderCommand));
    }

    @Test
    void payOrder_shouldPayOrderSuccessfully() {
        when(readOrderRepository.getOrderById(orderId)).thenReturn(order);
        when(writeOrderRepository.save(any(Order.class))).thenReturn(order);

        Order paidOrder = writeOrdersService.payOrder(orderId);

        assertNotNull(paidOrder);
        verify(writeOrderRepository).save(any(Order.class));
    }

    @Test
    void payOrder_shouldThrowOrderNotFoundException() {
        when(readOrderRepository.getOrderById(orderId)).thenReturn(null);

        assertThrows(OrderNotFoundException.class, () -> writeOrdersService.payOrder(orderId));
    }

    @Test
    void payOrder_payingForPaidOrderResultInException() {
        when(readOrderRepository.getOrderById(orderId)).thenReturn(order);
        when(writeOrderRepository.save(any(Order.class))).thenReturn(order);

        writeOrdersService.payOrder(orderId);

        assertThrows(ValidationException.class, () -> writeOrdersService.payOrder(orderId));
    }

    @Test
    void scheduleCancelUnfinishedOrders_shouldCancelOrdersSuccessfully() {
        writeOrdersService.cancelOrder(order);

        verify(writeOrderRepository).save(any(Order.class));
        verify(writeProductsRepository).saveAll(anyList());
        assertEquals(OrderStatus.CANCELED, order.getStatus());
        assertNull(order.getOrderItems().getFirst().getProduct());
    }
}
