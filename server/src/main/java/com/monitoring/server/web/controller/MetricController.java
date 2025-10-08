package com.monitoring.server.web.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monitoring.common.Metric;
import com.monitoring.server.web.service.MetricService;

@RestController
@RequestMapping("/api/metrics")
public class MetricController {

    private MetricService metricService;

    public MetricController(MetricService metricService) {
        this.metricService = metricService;
    }

    @GetMapping("/getFirstMetric") 
    public Metric getFirstMetric() {
        return metricService.getFirstMetric();
    }

    @GetMapping("/getMetrics")
    public List<Metric> getMetrics() {
        return metricService.getMetrics();
    }
}
