package com.monitoring.server.scheduler;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.monitoring.server.repository.MetricsRepository;

import jakarta.transaction.Transactional;

@Service
public class DataCleanupScheduler {
    private final MetricsRepository metricsRepository;
    private final long cutoffPeriodSeconds;

    public DataCleanupScheduler(MetricsRepository metricsRepository,
            @Value("${app.data.retention.seconds:60}") long cutoffPeriodSeconds) {
        this.metricsRepository = metricsRepository;
        this.cutoffPeriodSeconds = cutoffPeriodSeconds;
    }

    // cleaning up every minute
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void cleanUp() {
        try {
            LocalDateTime cutoffDate = LocalDateTime.now().minusSeconds(cutoffPeriodSeconds);
            System.out.println("Cleaning old metrics");
            metricsRepository.deleteOldMetrics(cutoffDate);
        } catch (Exception e) {
            System.out.println("Error during data cleanup: " + e);
        }
    }
}
