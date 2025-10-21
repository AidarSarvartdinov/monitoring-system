package com.monitoring.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monitoring.server.entity.Metrics;

@Repository
public interface MetricsRepository extends JpaRepository<Metrics, Long> {
    
}
