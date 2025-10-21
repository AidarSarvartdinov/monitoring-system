package com.monitoring.server.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.monitoring.server.entity.Metrics;

@Repository
public interface MetricsRepository extends JpaRepository<Metrics, Long> {
    @Modifying
    @Query("DELETE FROM Metrics m WHERE m.timestamp < :cutoffDate")
    void deleteOldMetrics(@Param("cutoffDate") LocalDateTime cutoffDate);
}
