package com.practice.hashing.production;

import java.util.concurrent.atomic.AtomicLongArray;

/**
 * A highly concurrent, thread-safe bit array implementation backed by an {@link AtomicLongArray}.
 * 
 * Design Choices:
 * - Why AtomicLongArray? Standard {@link java.util.BitSet} is not thread-safe. Wrapping it with 
 *   read/write locks would create high contention under heavy concurrent load (e.g., in a high-throughput 
 *   distributed system using a Bloom Filter). AtomicLongArray allows us to perform lock-free 
 *   Compare-And-Swap (CAS) operations at the word level (64 bits), enabling massive concurrency 
 *   without blocking threads.
 * - Longs vs Ints? A 64-bit 'long' allows us to pack 64 bits into a single CAS operation, 
 *   reducing the memory overhead of the array references and maximizing the amount of data 
 *   we can update atomically compared to a 32-bit 'int'.
 */
public class ConcurrentBitArray {
    
    // Each long holds 64 bits
    private static final int ADDRESS_BITS_PER_WORD = 6;
    private static final int BITS_PER_WORD = 1 << ADDRESS_BITS_PER_WORD;
    private static final int BIT_INDEX_MASK = BITS_PER_WORD - 1;

    private final AtomicLongArray data;
    private final long bitCount;

    /**
     * Creates a new concurrent bit array.
     *
     * @param bits the total number of bits required.
     */
    public ConcurrentBitArray(long bits) {
        this.bitCount = bits;
        // Calculate the number of 64-bit longs needed
        int arrayLength = (int) Math.ceil((double) bits / BITS_PER_WORD);
        this.data = new AtomicLongArray(arrayLength);
    }

    /**
     * Sets the bit at the specified index to true.
     * Uses a lock-free CAS loop to ensure thread safety without blocking.
     *
     * @param bitIndex the index of the bit to set
     * @return true if the bit was newly set, false if it was already true
     */
    public boolean set(long bitIndex) {
        if (bitIndex < 0 || bitIndex >= bitCount) {
            throw new IndexOutOfBoundsException("Bit index out of range: " + bitIndex);
        }

        int wordIndex = (int) (bitIndex >>> ADDRESS_BITS_PER_WORD);
        long bitMask = 1L << (bitIndex & BIT_INDEX_MASK);

        while (true) {
            long currentWord = data.get(wordIndex);
            
            // If the bit is already set, return false
            if ((currentWord & bitMask) != 0) {
                return false;
            }

            long newWord = currentWord | bitMask;
            
            // Atomically try to set the new word. If another thread modified the word 
            // in the meantime, CAS will fail and the loop will retry.
            if (data.compareAndSet(wordIndex, currentWord, newWord)) {
                return true;
            }
        }
    }

    /**
     * Returns the value of the bit with the specified index.
     * Lock-free read operation.
     *
     * @param bitIndex the index of the bit
     * @return true if the bit is currently set, false otherwise
     */
    public boolean get(long bitIndex) {
        if (bitIndex < 0 || bitIndex >= bitCount) {
            throw new IndexOutOfBoundsException("Bit index out of range: " + bitIndex);
        }

        int wordIndex = (int) (bitIndex >>> ADDRESS_BITS_PER_WORD);
        long bitMask = 1L << (bitIndex & BIT_INDEX_MASK);

        long currentWord = data.get(wordIndex);
        return (currentWord & bitMask) != 0;
    }

    /**
     * Returns the total number of bits this array can hold.
     *
     * @return the bit capacity
     */
    public long bitSize() {
        return bitCount;
    }
}
