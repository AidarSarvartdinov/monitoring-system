package com.monitoring.server.entity;

import java.time.LocalDateTime;

import com.monitoring.common.Metric;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Metrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private int port;
    private double value;
    private String type;
    private LocalDateTime timestamp;

    public Metrics(String url, int port, double value, String type, LocalDateTime timestamp) {
        this.url = url;
        this.port = port;
        this.value = value;
        this.type = type;
        this.timestamp = timestamp;
    }

    public Metrics(Metric metric) {
        this.url = metric.getServiceName().getUrl();
        this.port = metric.getServiceName().getPort();
        this.value = metric.getValue();
        this.type = metric.getType().toString();
        this.timestamp = metric.getTimestamp();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
