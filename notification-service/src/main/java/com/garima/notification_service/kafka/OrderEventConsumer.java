package com.garima.notification_service.kafka;

import com.garima.notification_service.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Component
@Slf4j
public class OrderEventConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "order-created",
            groupId = "notification-group-v99"
    )
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