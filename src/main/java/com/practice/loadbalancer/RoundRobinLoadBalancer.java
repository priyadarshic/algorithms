package com.practice.loadbalancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implements the Round Robin load balancing algorithm.
 * Distributes requests sequentially across the available servers.
 */
public class RoundRobinLoadBalancer implements LoadBalancer {
    private final AtomicInteger position = new AtomicInteger(0);

    @Override
    public Server getServer(List<Server> servers, String clientIp) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }
        
        // Get the current position and increment it modulo the server list size
        int index = position.getAndIncrement() % servers.size();
        
        // Handle negative result from possible overflow (though AtomicInteger.getAndIncrement() 
        // behavior with overflow is technically standard, we'll be safe)
        if (index < 0) index = Math.abs(index);
        
        return servers.get(index % servers.size());
    }
}
