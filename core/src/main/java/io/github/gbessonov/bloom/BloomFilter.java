package io.github.gbessonov.bloom;

import java.io.IOException;

/**
 * A generic Bloom Filter interface for probabilistic set membership testing.
 *
 * <p>This interface defines the core operations for a Bloom Filter, a space-efficient
 * probabilistic data structure that allows for testing whether an element is a member
 * of a set. False positive matches are possible, but false negatives are not.
 *
 * @param <T> the type of elements to be stored in the Bloom Filter
 */
public interface BloomFilter<T> {
    /**
     * Tests whether the specified object might have been added to the Bloom Filter.
     *
     * <p>This method can return false if the object is definitely not in the filter,
     * or true if the object might be in the filter (with some false positive probability).
     *
     * @param object the object to check for membership
     * @return {@code true} if the object might be present, {@code false} if definitely not
     */
    boolean mightContain(T object);

    /**
     * Adds the specified object to the Bloom Filter.
     *
     * @param object the object to add
     */
    void put(T object);

    /**
     * Returns the expected false positive probability (FPP) of the Bloom Filter.
     *
     * <p>The FPP indicates the likelihood that {@link #mightContain(Object)} will
     * return {@code true} for an object that has not actually been added.
     *
     * @return the expected false positive probability
     */
    double expectedFpp();

    /**
     * Calculates the utilization of the Bloom filter's bit array.
     *
     * <p>Utilization is defined as the ratio of bits that are currently set
     * to the total number of bits in the filter. A higher utilization indicates
     * that more bits are occupied, which can lead to a higher false positive
     * probability (FPP). This metric is useful for monitoring the saturation
     * level of the filter.
     *
     * @return a double value between 0.0 and 1.0 representing the fraction of bits set
     */
    double utilization();

    /**
     * Returns an approximate count of unique elements that have been added to the Bloom Filter.
     *
     * <p>Note that due to the probabilistic nature of the structure, this count is approximate.
     *
     * @return the approximate number of elements added
     */
    long approximateElementCount();

    /**
     * Serializes the Bloom filter's internal BitSet to a byte array.
     *
     * <p>This method can be used to persist the state of the Bloom filter
     * or transmit it over a network. The serialization format used is Java's
     * built-in object serialization (i.e., {@link java.io.ObjectOutputStream}),
     * so the resulting byte array can be deserialized using a compatible method
     * like {@code fromBytes(byte[])}.
     *
     * @return a byte array representing the serialized form of the Bloom filter
     * @throws IOException if an I/O error occurs during serialization
     */
    byte[] toBytes() throws IOException;
}
