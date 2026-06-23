package com.garima.analytics_service.service;

import com.garima.analytics_service.dto.AnalyticsResponse;
import com.garima.analytics_service.entity.AnalyticsSummary;
import com.garima.analytics_service.repository.AnalyticsSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsSummaryRepository analyticsSummaryRepository;

    public AnalyticsResponse getAnalytics() {
        AnalyticsSummary summary =
                analyticsSummaryRepository.findById(1L)
                        .orElseGet(() -> {
                            AnalyticsSummary analytics =
                                    new AnalyticsSummary();

                            analytics.setId(1L);
                            analytics.setTotalOrders(0L);
                            analytics.setTotalRevenue(0.0);

                            return analytics;
                        });
        AnalyticsResponse response = new AnalyticsResponse();
        response.setTotalOrders(summary.getTotalOrders());
        response.setTotalRevenue(summary.getTotalRevenue());

        return response;
    }
}
