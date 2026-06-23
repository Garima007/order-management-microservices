package com.garima.analytics_service.repository;

import com.garima.analytics_service.entity.AnalyticsSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticsSummaryRepository
        extends JpaRepository<AnalyticsSummary, Long> {
}