package io.github.gbessonov.bloom;

import io.github.gbessonov.bloom.implementation.BasicBloomFilter;

public final class BloomFilters {
    private BloomFilters() {
    }

    public static <T> BloomFilter<T> create() {
        return new BasicBloomFilter<>();
    }
}
