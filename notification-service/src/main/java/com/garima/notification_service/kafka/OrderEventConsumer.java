package com.garima.notification_service.kafka;

import com.garima.notification_service.event.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderEventConsumer {

    @KafkaListener(
            topics = "order-created",
            groupId = "notification-group-v99"
    )
    public void consume(OrderCreatedEvent event) {

        log.info(
                "Notification sent for Order ID: {}, User ID: {}",
                event.getOrderId(),
                event.getUserId()
        );
    }
}
