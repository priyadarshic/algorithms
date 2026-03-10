package com.algo.graphs;

import java.util.Collection;

/**
 * Interface representing a Graph data structure.
 * @param <V> Vertex type
 */
public interface Graph<V> {
    void addVertex(V v);
    void addEdge(V source, V destination);
    Collection<V> getNeighbors(V v);
    Collection<V> getAllVertices();
}
