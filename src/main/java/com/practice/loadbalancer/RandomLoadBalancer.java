package com.practice.loadbalancer;

import java.util.List;
import java.util.Random;

/**
 * Implements the Random load balancing algorithm.
 * Randomly picks a server from the available pool.
 */
public class RandomLoadBalancer implements LoadBalancer {
    private final Random random = new Random();

    @Override
    public Server getServer(List<Server> servers, String clientIp) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }

        int index = random.nextInt(servers.size());
        return servers.get(index);
    }
}
