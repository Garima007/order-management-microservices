package com.garima.analytics_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "analytics_summary")
@Data
public class AnalyticsSummary {

    @Id
    private Long id;

    private Long totalOrders;

    private Double totalRevenue;
}