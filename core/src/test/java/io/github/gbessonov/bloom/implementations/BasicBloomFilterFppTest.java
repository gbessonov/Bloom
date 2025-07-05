package io.github.gbessonov.bloom.implementations;

import io.github.gbessonov.bloom.BloomFilter;
import io.github.gbessonov.bloom.BloomFilters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BasicBloomFilterFppTest {

    @Test
    public void SmokeTest() {
        BloomFilter<Object> bloomFilter = BloomFilters.create();
        Assertions.assertEquals(0, bloomFilter.expectedFpp());
    }
}
