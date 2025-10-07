package com.monitoring.server.socket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.monitoring.common.AgentMessage;
import com.monitoring.common.Metric;

public class MonitoringServer {
    private int port;
    private volatile boolean running;
    private ServerSocket server;
    private ExecutorService executor;

    public MonitoringServer(int port) {
        this.port = port;
        this.executor = Executors.newCachedThreadPool();
    }

    public void start() {
        try {
            server = new ServerSocket(port);
            this.running = true;
            System.out.println("Starting Monitoring Server");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Recieved shutdown signal");
                stop();
            }));

            while (running) {
                Socket agent = server.accept();

                executor.submit(() -> {
                    if (!running)
                        return;
                    String agentInfo = agent.getRemoteSocketAddress().toString();
                    System.out.println("Starting handling agent " + agentInfo);
                    try (ObjectInputStream in = new ObjectInputStream(agent.getInputStream());
                            ObjectOutputStream out = new ObjectOutputStream(agent.getOutputStream())) {
                        while (running && !agent.isClosed()) {
                            try {
                                Object input = in.readObject();
                                if (input instanceof AgentMessage) {
                                    System.out.println(input);
                                } else {
                                    Metric metric = (Metric) input;
                                    System.out.println(metric);

                                }
                            } catch (EOFException e) {
                                System.out.println("Agent " + agentInfo + " disconnected");
                                break;
                            } catch (ClassNotFoundException e) {
                                System.err.println("Unknown object from " + agentInfo + ": " + e);
                                break;
                            } catch (IOException e) {
                                if (running) {
                                    System.err.println("Connection error with " + agentInfo + ": " + e);
                                } else {
                                    System.out.println("Agent " + agentInfo + " disconnected during shutdown");
                                }
                                
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            agent.close();
                        } catch (IOException e) {
                            System.err.println("Error closing agent " + agentInfo + ": " + e);
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        System.out.println("Stopping Monitoring Server");

        synchronized (this) {
            running = false;
        }

        try {
            if (server != null) {
                server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        executor.shutdown();

        try {
            if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                executor.shutdownNow();

            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

    }

}
