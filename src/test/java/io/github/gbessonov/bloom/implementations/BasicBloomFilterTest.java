package io.github.gbessonov.bloom.implementations;

import io.github.gbessonov.bloom.BloomFilters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class BasicBloomFilterTest {
    @ParameterizedTest
    @CsvSource({
            "42",
            "Hello world!",
            "Ahoj",
            "↓←☺",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque tristique, leo quis sollicitudin efficitur, leo nulla malesuada est, eu auctor mauris nisi at tellus. Integer vel tortor nec nisl rutrum volutpat ut at lacus. Ut at tellus congue nunc auctor iaculis. Cras lacinia scelerisque justo vel imperdiet. Fusce semper augue vel justo venenatis, consequat pellentesque turpis facilisis. Vestibulum faucibus quam massa, et feugiat justo accumsan ultrices. Cras quis purus accumsan, vehicula nisi hendrerit, mattis metus. Sed nec lacus tempor nisl lobortis vehicula et eu orci. Vivamus tristique, lectus sit amet eleifend congue, dui ante tempor ligula, a sollicitudin erat elit non lorem. Praesent hendrerit justo mollis, tristique ligula ac, scelerisque libero. Curabitur sit amet diam eu nisi vestibulum congue ac eget ligula"
    })
    public void PresenceTest(Object value) {
        var bloomFilter = BloomFilters.create();
        Assertions.assertFalse(bloomFilter.mightContain(value));

        bloomFilter.put(value);
        Assertions.assertTrue(bloomFilter.mightContain(value));
    }
}
