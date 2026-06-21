package com.garima.order_service.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderCreatedEvent {

    private String eventId;
    private Long orderId;
    private Long userId;
    private Double amount;
    private LocalDateTime timestamp;
}