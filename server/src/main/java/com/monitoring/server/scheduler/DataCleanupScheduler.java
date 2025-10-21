package com.monitoring.server.scheduler;

import org.springframework.stereotype.Service;

import com.monitoring.server.repository.MetricsRepository;

@Service
public class DataCleanupScheduler {
    private final MetricsRepository metricsRepository;

    public DataCleanupScheduler(MetricsRepository metricsRepository) {
        this.metricsRepository = metricsRepository;
    }

    
}
