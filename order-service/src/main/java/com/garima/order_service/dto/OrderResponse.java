package com.garima.order_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderResponse {
    @NotBlank
    private Long id;
    @NotBlank
    private String productName;
    @NotNull
    @Positive
    private Double amount;
    @NotBlank
    private Long userId;
}