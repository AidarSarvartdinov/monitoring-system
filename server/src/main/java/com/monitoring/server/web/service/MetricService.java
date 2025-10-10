package com.monitoring.server.web.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.monitoring.common.Metric;
import com.monitoring.server.web.dto.MetricDto;

@Service
public class MetricService {
    private List<Metric> metrics = new CopyOnWriteArrayList<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final SimpMessagingTemplate messagingTemplate;

    public MetricService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void addMetric(Metric metric) {
        metrics.add(metric);
        sendMetric(metric);
        if (metrics.size() >= 1000) {
            metrics.remove(0);
        }
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
