package com.practice.hashing.production;

/**
 * Listener interface for reacting to topology changes in the consistent hash ring.
 */
public interface RingChangeListener {

    /**
     * Called when a new physical node is successfully added to the ring.
     * @param nodeId the ID of the node that was added
     */
    void onNodeAdded(String nodeId);

    /**
     * Called when a physical node is removed from the ring.
     * @param nodeId the ID of the node that was removed
     */
    void onNodeRemoved(String nodeId);
}
