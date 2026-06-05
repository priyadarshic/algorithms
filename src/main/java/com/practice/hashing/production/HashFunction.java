package com.practice.hashing.production;

/**
 * Interface defining the hash function used for placing nodes and keys on the ring.
 * Implementing this interface allows injecting custom hashing logic (e.g., MD5, SHA-256).
 */
public interface HashFunction {
    
    /**
     * Hashes the given string key into a 32-bit integer.
     *
     * @param key the string to hash
     * @return the 32-bit integer hash
     */
    int hash(String key);
}
