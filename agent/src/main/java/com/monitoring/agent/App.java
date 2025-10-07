package com.monitoring.agent;

import com.monitoring.common.Service;

public class App {
    public static void main(String[] args) {
        MonitoringAgent agent1 = new MonitoringAgent(1, new Service("localhost", 8081));
        MonitoringAgent agent2 = new MonitoringAgent(2, new Service("localhost", 8081));

        agent1.addService(new Service("google.com", 8080));
        agent1.addService(new Service("yandex.ru", 8082));
        agent1.start();
        
        agent2.start();
        agent2.addService(new Service("ServiceFromAgent2", 4483));
    }
}
