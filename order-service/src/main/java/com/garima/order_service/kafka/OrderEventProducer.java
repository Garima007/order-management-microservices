package com.garima.order_service.kafka;

import com.garima.order_service.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @Value("${app.kafka.topics.order-created}")
    private String orderCreatedTopic;

    public void publishOrderCreated(OrderCreatedEvent event) {

        kafkaTemplate.send(
                orderCreatedTopic,
                event.getOrderId().toString(),
                event
        );
    }
}