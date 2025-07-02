package io.github.gbessonov.bloom.hashing;

import io.github.gbessonov.bloom.HashCode;
import io.github.gbessonov.bloom.HashFunction;

/**
 * Implementation of MurmurHash3 (x64 128-bit variant).
 * Designed for high-performance non-cryptographic hashing,
 * often used in Bloom filters and other probabilistic data structures.
 */
public class Murmur3f implements HashFunction {

    // Constants defined by the MurmurHash3 algorithm
    private static final long C1 = 0x87c37b91114253d5L;
    private static final long C2 = 0x4cf5ad432745937fL;

    // Internal hash state (64 bits each)
    private long h1;
    private long h2;

    // Tracks total number of bytes hashed
    private int length;

    public Murmur3f() {
        this(0); // default seed = 0
    }

    public Murmur3f(int seed) {
        reset(seed);
    }

    /**
     * Includes the given input into the hash computation.
     * Can be called multiple times to hash concatenated data streams.
     */
    @Override
    public HashFunction include(byte[] input) {
        int i = 0;
        int limit = input.length - 15;

        // Process full 128-bit (16-byte) blocks
        while (i <= limit) {
            long k1 = getLittleEndianLong(input, i);
            long k2 = getLittleEndianLong(input, i + 8);
            bmix64(k1, k2);
            i += 16;
            length += 16;
        }

        // Process remaining (non-aligned) tail bytes
        processRemaining(input, i);
        return this;
    }

    /**
     * Reads 8 bytes from the byte array starting at the given offset
     * and interprets them as a little-endian long.
     */
    private static long getLittleEndianLong(byte[] data, int offset) {
        return ((long) data[offset] & 0xff) |
                (((long) data[offset + 1] & 0xff) << 8) |
                (((long) data[offset + 2] & 0xff) << 16) |
                (((long) data[offset + 3] & 0xff) << 24) |
                (((long) data[offset + 4] & 0xff) << 32) |
                (((long) data[offset + 5] & 0xff) << 40) |
                (((long) data[offset + 6] & 0xff) << 48) |
                (((long) data[offset + 7] & 0xff) << 56);
    }

    /**
     * Processes the final remaining bytes of the input that did not fit
     * into a full 16-byte block. Handles 1–15 tail bytes.
     */
    protected void processRemaining(byte[] data, int offset) {
        int remaining = data.length - offset;
        length += remaining;

        long k1 = 0;
        long k2 = 0;

        // Construct partial k1 and k2 values from tail bytes (little-endian)
        switch (remaining) {
            case 15: k2 ^= (data[offset + 14] & 0xffL) << 48;
            case 14: k2 ^= (data[offset + 13] & 0xffL) << 40;
            case 13: k2 ^= (data[offset + 12] & 0xffL) << 32;
            case 12: k2 ^= (data[offset + 11] & 0xffL) << 24;
            case 11: k2 ^= (data[offset + 10] & 0xffL) << 16;
            case 10: k2 ^= (data[offset + 9] & 0xffL) << 8;
            case 9:  k2 ^=  data[offset + 8] & 0xffL;
            case 8:  k1 ^= getLittleEndianLong(data, offset); break;
            case 7:  k1 ^= (data[offset + 6] & 0xffL) << 48;
            case 6:  k1 ^= (data[offset + 5] & 0xffL) << 40;
            case 5:  k1 ^= (data[offset + 4] & 0xffL) << 32;
            case 4:  k1 ^= (data[offset + 3] & 0xffL) << 24;
            case 3:  k1 ^= (data[offset + 2] & 0xffL) << 16;
            case 2:  k1 ^= (data[offset + 1] & 0xffL) << 8;
            case 1:  k1 ^=  data[offset] & 0xffL; break;
            case 0:  break;
            default:
                throw new AssertionError("Unexpected tail size: " + remaining);
        }

        // Mix partial blocks into state
        h1 ^= mixK1(k1);
        h2 ^= mixK2(k2);
    }

    /**
     * Finalizes the hash computation and returns the resulting 128-bit hash code.
     */
    @Override
    public HashCode hash() {
        return prepareHashCode(h1, h2, length);
    }

    /**
     * Resets the internal state of the hash function to allow reuse.
     *
     * @param seed initial seed to reset the function with
     */
    @Override
    public void reset(int seed) {
        this.h1 = seed;
        this.h2 = seed;
        this.length = 0;
    }

    /**
     * Mixes a 128-bit input block into the internal state.
     * This is the core compression function of Murmur3.
     */
    private void bmix64(long k1, long k2) {
        h1 ^= mixK1(k1);
        h1 = Long.rotateLeft(h1, 27);
        h1 += h2;
        h1 = h1 * 5 + 0x52dce729;

        h2 ^= mixK2(k2);
        h2 = Long.rotateLeft(h2, 31);
        h2 += h1;
        h2 = h2 * 5 + 0x38495ab5;
    }

    /**
     * Mixes k1 input into a scrambled long as per Murmur3 spec.
     */
    private static long mixK1(long k1) {
        k1 *= C1;
        k1 = Long.rotateLeft(k1, 31);
        k1 *= C2;
        return k1;
    }

    /**
     * Mixes k2 input into a scrambled long as per Murmur3 spec.
     */
    private static long mixK2(long k2) {
        k2 *= C2;
        k2 = Long.rotateLeft(k2, 33);
        k2 *= C1;
        return k2;
    }

    /**
     * Final avalanche mixing step to finalize h1 and h2.
     * Ensures avalanche effect and uniform distribution.
     */
    private static long fmix64(long k) {
        k ^= k >>> 33;
        k *= 0xff51afd7ed558ccdL;
        k ^= k >>> 33;
        k *= 0xc4ceb9fe1a85ec53L;
        k ^= k >>> 33;
        return k;
    }

    /**
     * Performs finalization and returns the resulting HashCode object.
     */
    private static HashCode prepareHashCode(long h1, long h2, int length) {
        h1 ^= length;
        h2 ^= length;

        h1 += h2;
        h2 += h1;

        h1 = fmix64(h1);
        h2 = fmix64(h2);

        h1 += h2;
        h2 += h1;

        return new Murmur3fHashCode(h1, h2);
    }
}
