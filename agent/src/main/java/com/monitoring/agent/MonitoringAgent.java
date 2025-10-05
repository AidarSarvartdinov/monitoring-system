package com.monitoring.agent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.monitoring.common.AgentMessage;
import com.monitoring.common.Metric;
import com.monitoring.common.Service;
import com.monitoring.common.Metric.MetricType;

public class MonitoringAgent {
    private int agentId;
    private List<Service> monitoredServices;
    private AtomicBoolean running = new AtomicBoolean(false);
    private ScheduledExecutorService scheduler;
    private Service server;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Random random = new Random();

    public MonitoringAgent(int agentId, Service server) {
        this.agentId = agentId;
        this.monitoredServices = new ArrayList<>();
        this.scheduler = Executors.newScheduledThreadPool(3);
        this.server = server;
    }

    public void start() {
        if (!this.running.compareAndSet(false, true)) {
            System.out.println("Agent " + agentId + " already running");
            return;
        }

        try {
            this.socket = new Socket(server.getUrl(), server.getPort());
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out.writeObject(new AgentMessage(agentId, "CONNECTED"));
            out.flush();
            System.out.println("Agent " + agentId + " connected to server " + server);
            startSendingMetrics();
        } catch (IOException e) {
            System.err.println("Failed to start agent " + agentId + ": " + e.getMessage());
            running.set(false);
        }
    }

    public void stop() {
        if (!running.compareAndSet(true, false)) {
            return;
        }

        System.out.println("Stopping agent " + agentId);

        if (out != null) {
            try {
                synchronized (out) {
                    out.writeObject(new AgentMessage(agentId, "DISCONNECTED"));
                    out.flush();
                }
            } catch (IOException e) {
                System.err.println("Faild to send disconnect message: " + e.getMessage());
            }
        }

        if (scheduler != null) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        try {
            if (out != null)
                out.close();
            if (in != null)
                in.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            System.err.println("Error closing resourses: " + e.getMessage());
        }

        System.out.println("Agent " + agentId + " stopped");
    }

    private void startSendingMetrics() {
        for (Service service : monitoredServices) {
            startMonitoringForService(service);
        }
    }

    private void startMonitoringForService(Service service) {
        scheduler.scheduleAtFixedRate(() -> {
            if (!running.get())
                return;
            try {
                for (Metric metric : getAllMetrics(service)) {
                    synchronized (out) {
                        out.writeObject(metric);
                        out.flush();
                    }
                }
            } catch (IOException e) {
                System.err.println("Error sending metrics " + e);
                stop();
            }
        }, 1, 5, TimeUnit.SECONDS);
    }

    public void addService(Service service) {
        if (!monitoredServices.contains(service)) {
            monitoredServices.add(service);
            System.out.println("Added service " + service);
            if (running.get()) {
                startMonitoringForService(service);
            }
        }
    }

    private List<Metric> getAllMetrics(Service service) {
        return List.of(getCPUUsage(service), getResponseTime(service), getMemoryUsage(service), getDiskUsage(service));
    }

    private Metric getCPUUsage(Service service) {
        return new Metric(agentId, service, random.nextDouble(), MetricType.CPU_USAGE);
    }

    private Metric getResponseTime(Service service) {
        return new Metric(agentId, service, random.nextDouble(), MetricType.RESPONSE_TIME);
    }

    private Metric getMemoryUsage(Service service) {
        return new Metric(agentId, service, random.nextDouble(), MetricType.MEMORY_USAGE);
    }

    private Metric getDiskUsage(Service service) {
        return new Metric(agentId, service, random.nextDouble(), MetricType.DISK_USAGE);
    }
}
