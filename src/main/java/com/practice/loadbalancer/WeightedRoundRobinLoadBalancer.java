package com.practice.loadbalancer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implements the Weighted Round Robin load balancing algorithm.
 * Higher weighted servers get more traffic proportionally.
 */
public class WeightedRoundRobinLoadBalancer implements LoadBalancer {
    private final AtomicInteger position = new AtomicInteger(0);

    @Override
    public Server getServer(List<Server> servers, String clientIp) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }

        // Create a flattened list based on server weights
        // For example: S1 (weight 3), S2 (weight 1) -> [S1, S1, S1, S2]
        List<Server> weightedList = new ArrayList<>();
        for (Server server : servers) {
            for (int i = 0; i < server.getWeight(); i++) {
                weightedList.add(server);
            }
        }

        // Standard sequence distribution over the flattened list
        int index = position.getAndIncrement() % weightedList.size();
        if (index < 0) index = Math.abs(index);
        
        return weightedList.get(index % weightedList.size());
    }
}
