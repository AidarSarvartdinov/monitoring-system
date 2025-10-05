package com.monitoring.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Metric implements Serializable {
    private static final long serialVersionUID = 1L;

    private int agentId;
    private Service service;
    private double value;
    private MetricType type;
    private LocalDateTime timestamp;

    public enum MetricType {
        CPU_USAGE, RESPONSE_TIME,
        MEMORY_USAGE, DISK_USAGE
    }

    public Metric() {
        this.timestamp = LocalDateTime.now();
    }

    public Metric(int agentId, Service service, double value, MetricType type) {
        this();
        this.agentId = agentId;
        this.service = service;
        this.value = value;
        this.type = type;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public Service getServiceName() {
        return service;
    }

    public void setServiceName(Service service) {
        this.service = service;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public MetricType getType() {
        return type;
    }

    public void setType(MetricType type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Metric metric = (Metric) o;
        return Double.compare(value, metric.getValue()) == 0 &&
        agentId == metric.getAgentId() &&
        service.equals(metric.getServiceName()) && 
        type.compareTo(metric.getType()) == 0 &&
        timestamp.equals(metric.getTimestamp());   
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentId, service, value, type, timestamp);
    }

}
