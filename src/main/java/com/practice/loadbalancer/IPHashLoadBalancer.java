package com.practice.loadbalancer;

import java.util.List;

/**
 * Implements the IP Hash load balancing algorithm.
 * Selects a server based on the hash of the client IP to ensure affinity.
 */
public class IPHashLoadBalancer implements LoadBalancer {

    @Override
    public Server getServer(List<Server> servers, String clientIp) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }

        // Use the absolute value of the hash code to determine which server to pick
        int hash = Math.abs(clientIp.hashCode());
        int index = hash % servers.size();
        
        return servers.get(index);
    }
}
