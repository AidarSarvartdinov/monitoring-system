package com.monitoring.common;

public class AgentMessage {
    private int agentId;
    private String message;

    public AgentMessage(int agentId, String message) {
        this.agentId = agentId;
        this.message = message;
    }

    public int getAgentId() {
        return agentId;
    }

    public String getMessage() {
        return message;
    }

}
