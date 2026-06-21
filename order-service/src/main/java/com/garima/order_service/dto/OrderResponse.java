package com.garima.order_service.dto;

import lombok.Data;

@Data
public class OrderResponse {

    private Long id;

    private String productName;

    private Double amount;

    private Long userId;
}