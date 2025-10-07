package com.monitoring.server.socket;

public class App {
    public static void main(String[] args) {
        MonitoringServer server = new MonitoringServer(8081);
        server.start();
 
    }
}
