package com.monitoring.server.web.dto;

public record MetricDto(
        String url,
        int port,
        double value,
        String type,
        String timestamp) {
}
