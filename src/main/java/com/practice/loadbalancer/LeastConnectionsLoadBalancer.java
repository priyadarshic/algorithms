package com.practice.loadbalancer;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Implements the Least Connections load balancing algorithm.
 * Selects the server with the fewest active connections.
 */
public class LeastConnectionsLoadBalancer implements LoadBalancer {

    @Override
    public Server getServer(List<Server> servers, String clientIp) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }

        // Find the server with the minimum number of active connections
        Optional<Server> minServer = servers.stream()
                .min(Comparator.comparingInt(Server::getActiveConnections));
        
        return minServer.orElse(servers.get(0));
    }
}
