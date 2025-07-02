package io.github.gbessonov.bloom.hashing;

import io.github.gbessonov.bloom.HashCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.ByteBuffer;
import java.util.stream.Stream;

public class Murmur3fTest {

    private static Stream<Arguments> HashingTestDataProvider() {
        return Stream.of(
                Arguments.of(0, 0x629942693e10f867L, 0x92db0b82baeb5347L, "hell"),
                Arguments.of(1, 0xa78ddff5adae8d10L, 0x128900ef20900135L, "hello"),
                Arguments.of(2, 0x8a486b23f422e826L, 0xf962a2c58947765fL, "hello "),
                Arguments.of(3, 0x2ea59f466f6bed8cL, 0xc610990acc428a17L, "hello w"),
                Arguments.of(4, 0x79f6305a386c572cL, 0x46305aed3483b94eL, "hello wo"),
                Arguments.of(5, 0xc2219d213ec1f1b5L, 0xa1d8e2e0a52785bdL, "hello wor"),
                Arguments.of(0, 0xe34bbc7bbc071b6cL, 0x7a433ca9c49a9347L, "The quick brown fox jumps over the lazy dog"),
                Arguments.of(0, 0x658ca970ff85269aL, 0x43fee3eaa68e5c3eL, "The quick brown fox jumps over the lazy cog")
        );
    }

    @ParameterizedTest
    @MethodSource("HashingTestDataProvider")
    public void HashingTest(int seed, long expectedHash1, long expectedHash2, String inputString){
        HashCode expectedHash = new Murmur3fHashCode(expectedHash1, expectedHash2);

        var foo = new Murmur3f(seed);
        foo.include(ascii(inputString));
        var actualHash = foo.hash();

        Assertions.assertEquals(expectedHash, actualHash);
    }

    static byte[] ascii(String string) {
        byte[] bytes = new byte[string.length()];
        for (int i = 0; i < string.length(); i++) {
            bytes[i] = (byte) string.charAt(i);
        }
        return bytes;
    }
}
