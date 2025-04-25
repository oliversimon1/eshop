package com.oliver.eshop.service.order.scheduled;

import com.oliver.eshop.domain.Order;
import com.oliver.eshop.domain.OrderStatus;
import com.oliver.eshop.service.order.WriteOrdersService;
import com.oliver.eshop.service.order.port.ReadOrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class CancelUnfinishedOrderScheduler {

    private final ReadOrderRepository readOrderRepository;
    private final WriteOrdersService writeOrdersService;

    @Scheduled(cron = "0 0/1 * * * *")
    public void scheduleCancelUnfinishedOrders() {
        List<Order> ordersToCancel = readOrderRepository
                .getAllOrdersByStatusAndCreatedAtBefore(
                        OrderStatus.AWAITING_PAYMENT,
                        OffsetDateTime.now(ZoneOffset.UTC).minusMinutes(30)
                );

        ordersToCancel.forEach(writeOrdersService::cancelOrder);

        log.info("Cancelled {} orders.", ordersToCancel.size());
    }
}
