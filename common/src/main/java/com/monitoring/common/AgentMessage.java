package com.monitoring.common;

import java.io.Serializable;

public class AgentMessage implements Serializable {
    private static final long serialVersionUID = 1L;
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

    @Override
    public String toString() {
        return "Agent " + agentId + ": " + message;
    }
}
