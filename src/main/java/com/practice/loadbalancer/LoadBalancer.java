package com.practice.loadbalancer;

import java.util.List;

/**
 * Interface definition for a load balancer.
 */
public interface LoadBalancer {
    /**
     * Given a list of available servers, select one based on the implemented algorithm.
     * 
     * @param servers The list of backend servers in the pool.
     * @param clientIp The IP address of the user making the request.
     * @return The chosen server.
     */
    Server getServer(List<Server> servers, String clientIp);
}
