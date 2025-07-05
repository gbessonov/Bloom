package io.github.gbessonov.bloom.implementation;

import io.github.gbessonov.bloom.BloomFilter;

import java.io.*;
import java.util.BitSet;

/**
 * A basic, non-thread-safe Bloom filter implementation that uses
 * two hash functions derived from the 32-bit {@code hashCode()} method.
 *
 * <p>Not suitable for high-precision or concurrent use cases. For advanced
 * or thread-safe variants, use a more sophisticated implementation.
 *
 * @param <T> the type of elements to be inserted into the filter
 */
public class BasicBloomFilter<T> implements BloomFilter<T> {
    private final BitSet bits;

    public BasicBloomFilter() {
        this.bits = new BitSet(1 << 16);
    }

    @Override
    public boolean mightContain(T object) {
        var hash = object.hashCode();
        var hash1 = hash & (0xFFFF);
        var hash2 = (hash >> 16) & (0xFFFF);
        return bits.get(hash1) && bits.get(hash2);
    }

    @Override
    public void put(T object) {
        var hash = object.hashCode();
        var hash1 = hash & (0xFFFF);
        var hash2 = (hash >> 16) & (0xFFFF);
        bits.set(hash1);
        bits.set(hash2);
    }

    @Override
    public double expectedFpp() {
        double m = 1 << 16;
        double n = approximateElementCount();
        int k = 2;

        return Math.pow(1 - Math.exp(-k * n / m), k);
    }

    @Override
    public double utilization() {
        return (double) bits.cardinality() / bits.size();
    }

    @Override
    public long approximateElementCount() {
        double m = bits.size();
        double X = m - bits.cardinality();
        return (long) (-m * Math.log(X / m) / 2);
    }

    public byte[] toBytes() throws IOException {
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(out)
        ) {
            oos.writeObject(bits);
            oos.flush();
            return out.toByteArray();
        }
    }

    public static <T> BasicBloomFilter<T> fromBytes(byte[] data) throws IOException, ClassNotFoundException {
        if (data == null) {
            throw new IllegalArgumentException("data cannot be null");
        }
        try (
                InputStream input = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(input)
        ) {
            BitSet bitSet = (BitSet) ois.readObject();
            var filter = new BasicBloomFilter<T>();
            filter.bits.or(bitSet);
            return filter;
        }
    }

}
