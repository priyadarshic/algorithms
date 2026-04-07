package com.practice.loadbalancer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a backend server in the load balancing pool.
 */
public class Server {
    private final String ipAddress;
    private final int weight;
    private final AtomicInteger activeConnections;

    public Server(String ipAddress) {
        this(ipAddress, 1);
    }

    public Server(String ipAddress, int weight) {
        this.ipAddress = ipAddress;
        this.weight = weight;
        this.activeConnections = new AtomicInteger(0);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getWeight() {
        return weight;
    }

    public int getActiveConnections() {
        return activeConnections.get();
    }

    public void incrementConnections() {
        activeConnections.incrementAndGet();
    }

    public void decrementConnections() {
        activeConnections.decrementAndGet();
    }

    @Override
    public String toString() {
        return String.format("Server{ip='%s', weight=%d, active=%d}", 
                ipAddress, weight, activeConnections.get());
    }
}
