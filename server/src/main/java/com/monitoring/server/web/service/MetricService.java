package com.monitoring.server.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

import com.monitoring.common.Metric;

@Service
public class MetricService {
    private List<Metric> metrics = new CopyOnWriteArrayList<>();

    public void addMetric(Metric metric) {
        metrics.add(metric);
        if (metrics.size() >= 1000) {
            metrics.remove(0);
        }
    }

    public Metric getFirstMetric() {
        return metrics.get(0);
    }

    public List<Metric> getMetrics() {
        return metrics;
    }
}
