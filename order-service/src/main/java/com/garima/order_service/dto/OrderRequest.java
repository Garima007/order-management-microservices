package com.garima.order_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderRequest {
    @NotBlank
    private String productName;
    @NotNull
    @Positive
    private Double amount;
    @NotNull
    private Long userId;
    @NotNull
    private String header;

}