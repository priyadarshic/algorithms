package com.practice.loadbalancer;

import java.util.ArrayList;
import java.util.List;

/**
 * Demo class to demonstrate various load balancing algorithms.
 */
public class LoadBalancerDemo {
    public static void main(String[] args) {
        // Initialize servers with some weights
        List<Server> servers = new ArrayList<>();
        servers.add(new Server("192.168.1.1", 1)); // Low weight
        servers.add(new Server("192.168.1.2", 2)); // Medium weight
        servers.add(new Server("192.168.1.3", 3)); // High weight

        System.out.println("--- Server Pool ---");
        servers.forEach(System.out::println);
        System.out.println();

        // 1. Round Robin
        runDemo("Round Robin", new RoundRobinLoadBalancer(), servers, 6);

        // 2. Weighted Round Robin
        runDemo("Weighted Round Robin", new WeightedRoundRobinLoadBalancer(), servers, 6);

        // 3. Random
        runDemo("Random", new RandomLoadBalancer(), servers, 6);

        // 4. IP Hash
        runIPHashDemo(servers);

        // 5. Least Connections
        runLeastConnectionsDemo();
    }

    private static void runDemo(String name, LoadBalancer lb, List<Server> servers, int iterations) {
        System.out.println("--- " + name + " Load Balancer ---");
        for (int i = 1; i <= iterations; i++) {
            Server server = lb.getServer(servers, "127.0.0.1");
            System.out.printf("Request #%d: Routed to %s\n", i, server.getIpAddress());
        }
        System.out.println();
    }

    private static void runIPHashDemo(List<Server> servers) {
        System.out.println("--- IP Hash Load Balancer ---");
        LoadBalancer lb = new IPHashLoadBalancer();
        String clientA = "10.0.0.1";
        String clientB = "10.0.0.2";

        System.out.println("Client A (10.0.0.1) requests multiple times:");
        for (int i = 0; i < 3; i++) {
            System.out.printf("  Request #%d: Routed to %s\n", i + 1, lb.getServer(servers, clientA).getIpAddress());
        }

        System.out.println("Client B (10.0.0.2) requests multiple times:");
        for (int i = 0; i < 3; i++) {
            System.out.printf("  Request #%d: Routed to %s\n", i + 1, lb.getServer(servers, clientB).getIpAddress());
        }
        System.out.println();
    }

    private static void runLeastConnectionsDemo() {
        System.out.println("--- Least Connections Load Balancer ---");
        LoadBalancer lb = new LeastConnectionsLoadBalancer();
        List<Server> servers = new ArrayList<>();
        
        Server s1 = new Server("172.16.0.1");
        Server s2 = new Server("172.16.0.2");
        Server s3 = new Server("172.16.0.3");
        
        // Simulating different connection loads
        // s1: 10 connections, s2: 5 connections, s3: 15 connections
        for(int i=0; i<10; i++) s1.incrementConnections();
        for(int i=0; i<5; i++) s2.incrementConnections();
        for(int i=0; i<15; i++) s3.incrementConnections();
        
        servers.add(s1);
        servers.add(s2);
        servers.add(s3);

        System.out.println("Initial loads: S1:10, S2:5, S3:15");
        
        for (int i = 1; i <= 3; i++) {
            Server server = lb.getServer(servers, "127.0.0.1");
            System.out.printf("Request #%d: Routed to %s (current load: %d)\n", 
                    i, server.getIpAddress(), server.getActiveConnections());
            // Simulate the chosen server finishing its task and new ones arriving is skipped for simplicity
            // or we could increment to show it changes preference
            server.incrementConnections(); 
        }
        System.out.println();
    }
}
