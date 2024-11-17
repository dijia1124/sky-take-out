package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * handle orders that are not paid within allowed time
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void processTimedOutOrders() {
        log.info("Handling timed-out orders: {}", new Date());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);

        // select * from orders where status = 1 and create_time < current time minus 15 minutes
        List<Orders> orders = orderMapper.listByStatusAndOrderTime(Orders.PENDING_PAYMENT, time);

        if (orders != null && !orders.isEmpty()) {
            orders.forEach(order -> {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("Payment timed out and order cancelled");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            });
        }
    }

    /**
     * handle orders that are not completed
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder() {
        log.info("Handling delivery orders: {}", new Date());

        // select * from orders where status = 4 and create_time < current time minus 1 hour
        LocalDateTime time = LocalDateTime.now().plusHours(-1);
        List<Orders> orders = orderMapper.listByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, time);

        if (orders != null && !orders.isEmpty()) {
            orders.forEach(order -> {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            });
        }
    }
}
