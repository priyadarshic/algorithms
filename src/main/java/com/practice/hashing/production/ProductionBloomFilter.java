package com.practice.hashing.production;

import com.practice.hashing.MurmurHash3;

/**
 * A highly scalable, production-ready, thread-safe Bloom Filter.
 * 
 * Design Choices:
 * - Dynamic Sizing: Instead of a fixed-size array and fixed hashes, it calculates the optimal 
 *   bit array size 'm' and number of hash functions 'k' based on expected insertions ('n') 
 *   and desired false positive rate ('p').
 * - Double Hashing: Calling 'k' independent hash functions is expensive. We use the double 
 *   hashing technique from Kirsch and Mitzenmacher: hash_i(x) = (hash1(x) + i * hash2(x)) % m.
 *   This allows us to simulate 'k' hashes using just two underlying hash functions, significantly 
 *   improving performance.
 * - Concurrency: Backed by {@link ConcurrentBitArray} to allow lock-free, highly concurrent 
 *   reads and writes across multiple threads without synchronization bottlenecks.
 */
public class ProductionBloomFilter {

    private final ConcurrentBitArray bitArray;
    private final int numHashFunctions; // k

    /**
     * Private constructor. Use the static factory method to create an instance.
     */
    private ProductionBloomFilter(ConcurrentBitArray bitArray, int numHashFunctions) {
        this.bitArray = bitArray;
        this.numHashFunctions = numHashFunctions;
    }

    /**
     * Creates a new Bloom filter tailored to the expected number of items and desired accuracy.
     *
     * @param expectedInsertions the number of expected items to be inserted
     * @param falsePositiveProbability the acceptable false positive rate (e.g., 0.01 for 1%)
     * @return a configured ProductionBloomFilter
     */
    public static ProductionBloomFilter create(int expectedInsertions, double falsePositiveProbability) {
        if (expectedInsertions <= 0) {
            throw new IllegalArgumentException("Expected insertions must be positive");
        }
        if (falsePositiveProbability <= 0 || falsePositiveProbability >= 1) {
            throw new IllegalArgumentException("False positive probability must be > 0 and < 1");
        }

        // Calculate optimal 'm' (bit array size)
        // m = ceil(- (n * ln(p)) / (ln(2)^2))
        long optimalM = (long) Math.ceil(-1 * (expectedInsertions * Math.log(falsePositiveProbability)) 
                                         / Math.pow(Math.log(2), 2));
        
        // Calculate optimal 'k' (number of hash functions)
        // k = round((m / n) * ln(2))
        int optimalK = (int) Math.max(1, Math.round((double) optimalM / expectedInsertions * Math.log(2)));

        ConcurrentBitArray bitArray = new ConcurrentBitArray(optimalM);
        return new ProductionBloomFilter(bitArray, optimalK);
    }

    /**
     * Adds a string to the Bloom filter.
     *
     * @param data the string to add
     * @return true if the underlying bit array changed, false if all bits were already set
     */
    public boolean put(String data) {
        byte[] bytes = data.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        return putBytes(bytes);
    }

    /**
     * Adds a byte array to the Bloom filter.
     *
     * @param data the bytes to add
     * @return true if the underlying bit array changed, false if all bits were already set
     */
    public boolean putBytes(byte[] data) {
        long bitSize = bitArray.bitSize();
        
        // Generate two independent 32-bit hashes using MurmurHash3 with different seeds.
        // We use seed 0 for hash1 and seed 1 for hash2.
        int hash1 = MurmurHash3.hash32(data, 0);
        int hash2 = MurmurHash3.hash32(data, 1);

        boolean bitsChanged = false;
        long combinedHash = hash1;

        for (int i = 0; i < numHashFunctions; i++) {
            // Make the combined hash positive and map it to our bit array size
            // We use bitwise AND with Long.MAX_VALUE to drop the sign bit instead of Math.abs()
            // to avoid the Math.abs(Integer.MIN_VALUE) overflow issue.
            long index = (combinedHash & Long.MAX_VALUE) % bitSize;
            
            // set() returns true if the bit was newly set
            bitsChanged |= bitArray.set(index);
            
            // Double hashing formula: hash_i = hash1 + i * hash2
            // We iteratively add hash2 to combinedHash to avoid multiplying every loop
            combinedHash += hash2; 
        }

        return bitsChanged;
    }

    /**
     * Checks if a string might be in the Bloom filter.
     *
     * @param data the string to check
     * @return true if it MIGHT be present, false if it is DEFINITELY NOT present
     */
    public boolean mightContain(String data) {
        byte[] bytes = data.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        return mightContainBytes(bytes);
    }

    /**
     * Checks if a byte array might be in the Bloom filter.
     *
     * @param data the bytes to check
     * @return true if it MIGHT be present, false if it is DEFINITELY NOT present
     */
    public boolean mightContainBytes(byte[] data) {
        long bitSize = bitArray.bitSize();
        
        int hash1 = MurmurHash3.hash32(data, 0);
        int hash2 = MurmurHash3.hash32(data, 1);

        long combinedHash = hash1;

        for (int i = 0; i < numHashFunctions; i++) {
            long index = (combinedHash & Long.MAX_VALUE) % bitSize;
            
            // If any bit is not set, the item is definitely NOT in the filter
            if (!bitArray.get(index)) {
                return false;
            }
            
            combinedHash += hash2;
        }

        // All k bits were set, so it MIGHT be in the filter (or it's a false positive)
        return true;
    }

    // Getters for testing and inspection
    public long getBitSize() {
        return bitArray.bitSize();
    }

    public int getNumHashFunctions() {
        return numHashFunctions;
    }
}
