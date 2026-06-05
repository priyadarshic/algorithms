package com.practice.hashing.production;

import com.practice.hashing.MurmurHash3;

/**
 * An implementation of HashFunction that uses MurmurHash3.
 * MurmurHash3 provides excellent distribution properties and is extremely fast.
 */
public class MurmurHashFunction implements HashFunction {

    private final int seed;

    public MurmurHashFunction() {
        this(0);
    }

    public MurmurHashFunction(int seed) {
        this.seed = seed;
    }

    @Override
    public int hash(String key) {
        return MurmurHash3.hash32(key, seed);
    }
}
