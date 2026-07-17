package com.garima.analytics_service.kafka;

import com.garima.analytics_service.entity.AnalyticsSummary;
import com.garima.analytics_service.event.OrderCreatedEvent;
import com.garima.analytics_service.repository.AnalyticsSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class OrderEventConsumer {

    private final AnalyticsSummaryRepository repository;

    @KafkaListener(
            topics = "order-created",
            groupId = "analytics-group-v1"
    )
    public void consume(OrderCreatedEvent event) {

        AnalyticsSummary summary =
                repository.findById(1L)
                        .orElseGet(() -> {

                            AnalyticsSummary analytics =
                                    new AnalyticsSummary();

                            analytics.setId(1L);
                            analytics.setTotalOrders(0L);
                            analytics.setTotalRevenue(0.0);

                            return analytics;
                        });

        summary.setTotalOrders(
                summary.getTotalOrders() + 1
        );

        summary.setTotalRevenue(
                summary.getTotalRevenue() + event.getAmount()
        );

        repository.save(summary);

        log.info(
                "Analytics Updated. Orders={}, Revenue={}",
                summary.getTotalOrders(),
                summary.getTotalRevenue()
        );
    }
}
