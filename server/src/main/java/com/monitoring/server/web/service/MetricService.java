package com.monitoring.server.web.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.monitoring.common.Metric;
import com.monitoring.server.entity.Metrics;
import com.monitoring.server.repository.MetricsRepository;
import com.monitoring.server.web.dto.MetricDto;

@Service
public class MetricService {
    private final List<Metric> metrics = new CopyOnWriteArrayList<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final SimpMessagingTemplate messagingTemplate;
    private final MetricsRepository metricsRepository;

    public MetricService(SimpMessagingTemplate messagingTemplate, MetricsRepository metricsRepository) {
        this.messagingTemplate = messagingTemplate;
        this.metricsRepository = metricsRepository;
    }

    public void addMetric(Metric metric) {
        // metrics.add(metric);
        sendMetric(metric);
        metricsRepository.save(new Metrics(metric));
        // if (metrics.size() >= 1000) {
        //     metrics.remove(0);
        // }
    }

    public void sendMetric(Metric metric) {
        MetricDto metricDto = new MetricDto(
                metric.getServiceName().getUrl(),
                metric.getServiceName().getPort(),
                metric.getValue(),
                metric.getType().name(),
                formatter.format(metric.getTimestamp()));

        messagingTemplate.convertAndSend("/metrics", metricDto);
    }

    public Metric getFirstMetric() {
        return metrics.get(0);
    }

    public List<Metric> getMetrics() {
        return metrics;
    }
}
