package com.garima.analytics_service.dto;

import lombok.Data;

@Data
public class AnalyticsResponse {

    private Long totalOrders;
    private Double totalRevenue;
}