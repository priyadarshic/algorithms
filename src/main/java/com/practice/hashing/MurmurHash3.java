package com.practice.hashing;

public final class MurmurHash3 {

    // Magic constants from the MurmurHash3 spec
    private static final int C1 = 0xcc9e2d51;
    private static final int C2 = 0x1b873593;

    private MurmurHash3() {} // utility class

    /**
     * Computes MurmurHash3 (32-bit) for a byte array.
     *
     * @param data  the input bytes
     * @param seed  seed value (use 0 for a default hash)
     * @return      32-bit hash as an int
     */
    public static int hash32(byte[] data, int seed) {
        int h = seed;
        int length = data.length;
        int i = 0;

        // ── Stage 1 & 2: Process 4-byte blocks ───────────────────────────
        int nblocks = length / 4;

        for (int block = 0; block < nblocks; block++) {
            // Read 4 bytes as a little-endian int
            int k = getIntLE(data, i);
            i += 4;

            // Mix the block
            k *= C1;
            k = Integer.rotateLeft(k, 15);
            k *= C2;

            // Fold into running hash
            h ^= k;
            h = Integer.rotateLeft(h, 13);
            h = h * 5 + 0xe6546b64;
        }

        // ── Stage 3: Handle remaining 1–3 tail bytes ─────────────────────
        int tail = 0;
        int remaining = length & 3; // length % 4

        // Each case falls through intentionally — this is idiomatic Murmur
        switch (remaining) {
            case 3: tail ^= (data[i + 2] & 0xFF) << 16; // fall through
            case 2: tail ^= (data[i + 1] & 0xFF) << 8;  // fall through
            case 1:
                tail ^= (data[i] & 0xFF);
                tail *= C1;
                tail  = Integer.rotateLeft(tail, 15);
                tail *= C2;
                h    ^= tail;
        }

        // ── Stage 4: Finalizer (fmix) — force full avalanche ─────────────
        h ^= length;
        h  = fmix32(h);

        return h;
    }

    /** Convenience overload for String input (UTF-8). */
    public static int hash32(String text, int seed) {
        return hash32(text.getBytes(java.nio.charset.StandardCharsets.UTF_8), seed);
    }

    /**
     * fmix32 — the finalizer.
     * Ensures every bit of the input affects every bit of the output.
     * Without this, the lower bits would be under-mixed.
     */
    private static int fmix32(int h) {
        h ^= h >>> 16;
        h *= 0x85ebca6b;
        h ^= h >>> 13;
        h *= 0xc2b2ae35;
        h ^= h >>> 16;
        return h;
    }

    /** Read 4 bytes from data[offset] as a little-endian int. */
    private static int getIntLE(byte[] data, int offset) {
        return (data[offset]     & 0xFF)
                | (data[offset + 1] & 0xFF) << 8
                | (data[offset + 2] & 0xFF) << 16
                | (data[offset + 3] & 0xFF) << 24;
    }

    // ── Demo ──────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        String[] inputs = { "hello", "world", "hello world", "Murmur!" };

        System.out.printf("%-20s  %12s  %s%n", "Input", "Hash (signed)", "Hash (unsigned)");
        System.out.println("-".repeat(55));

        for (String s : inputs) {
            int h = hash32(s, 0);
            System.out.printf("%-20s  %12d  %d%n", "\"" + s + "\"", h, Integer.toUnsignedLong(h));
        }

        // Avalanche demo — flip one bit and see how different the hash is
        System.out.println("\n── Avalanche effect ──");
        int h1 = hash32("test", 0);
        int h2 = hash32("uest", 0); // 't' → 'u', one ASCII value apart
        System.out.printf("\"test\" → %d%n", Integer.toUnsignedLong(h1));
        System.out.printf("\"uest\" → %d%n", Integer.toUnsignedLong(h2));
        System.out.printf("Bits differing: %d / 32%n",
                Integer.bitCount(h1 ^ h2));
    }
}
