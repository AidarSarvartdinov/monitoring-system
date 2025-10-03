package main.java.com.monitoring.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Metric implements Serializable {
    private static final long serialVersionUID = 1L;

    private int agentId;
    private String serviceName;
    private double value;
    private MetricType type;
    private LocalDateTime timestamp;

    public enum MetricType {
        RPS, CPU_USAGE, RESPONSE_TIME,
        MEMORY_USAGE, DISK_USAGE
    }

    public Metric() {
        this.timestamp = LocalDateTime.now();
    }

    public Metric(int agentId, String serviceName, double value, MetricType type) {
        this();
        this.agentId = agentId;
        this.serviceName = serviceName;
        this.value = value;
        this.type = type;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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
    public boolean equal(Object o) {
        if (this == 0) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Metric metric = (Metric) o;
        return Double.compare(value, metric.getValue()) == 0 &&
        agentId == metric.getAgentId() &&
        serviceName.equals(metric.getServiceName()) && 
        type.compareTo(metric.getType()) == 0 &&
        timestamp.equals(metric.getTimestamp());   
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentId, serviceName, value, type, timestamp);
    }

}
