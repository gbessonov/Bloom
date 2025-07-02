package io.github.gbessonov.bloom.hashing;

import io.github.gbessonov.bloom.HashCode;
import io.github.gbessonov.bloom.HashFunction;

import java.nio.ByteBuffer;
import java.lang.Byte;
import java.nio.ByteOrder;

/* Murmur3F (MurmurHash3_x64_128) */
public class Murmur3f implements HashFunction {
    private static final long C1 = 0x87c37b91114253d5L;
    private static final long C2 = 0x4cf5ad432745937fL;

    private long h1;
    private long h2;
    private int length;

    public Murmur3f() {
        this(0);
    }

    public Murmur3f(int seed) {
        this.h1 = seed;
        this.h2 = seed;
        this.length = 0;
    }

    public void include(byte[] input) {
        int TWO_LONG_BYTES = 16;
        var buffer = ByteBuffer
                .wrap(input)
                .order(ByteOrder.LITTLE_ENDIAN);
        while (buffer.remaining() >= TWO_LONG_BYTES) {
            bmix64(buffer.getLong(), buffer.getLong());
            length += TWO_LONG_BYTES;
        }
        processRemaining(buffer);
    }

    protected void processRemaining(ByteBuffer bb) {
        long k1 = 0;
        long k2 = 0;
        int offset = bb.position();
        int remaining = bb.remaining();
        length += remaining;

        switch (remaining) {
            case 15:
                k2 ^= (long) Byte.toUnsignedInt(bb.get(offset + 14)) << 48;
            case 14:
                k2 ^= (long) Byte.toUnsignedInt(bb.get(offset + 13)) << 40;
            case 13:
                k2 ^= (long) Byte.toUnsignedInt(bb.get(offset + 12)) << 32;
            case 12:
                k2 ^= (long) Byte.toUnsignedInt(bb.get(offset + 11)) << 24;
            case 11:
                k2 ^= (long) Byte.toUnsignedInt(bb.get(offset + 10)) << 16;
            case 10:
                k2 ^= (long) Byte.toUnsignedInt(bb.get(offset + 9)) << 8;
            case 9:
                k2 ^= (long) Byte.toUnsignedInt(bb.get(offset + 8));
            case 8:
                k1 ^= bb.getLong(offset);
                break;
            case 7:
                k1 ^= (long) Byte.toUnsignedInt(bb.get(offset + 6)) << 48;
            case 6:
                k1 ^= (long) Byte.toUnsignedInt(bb.get(offset + 5)) << 40;
            case 5:
                k1 ^= (long) Byte.toUnsignedInt(bb.get(offset + 4)) << 32;
            case 4:
                k1 ^= (long) Byte.toUnsignedInt(bb.get(offset + 3)) << 24;
            case 3:
                k1 ^= (long) Byte.toUnsignedInt(bb.get(offset + 2)) << 16;
            case 2:
                k1 ^= (long) Byte.toUnsignedInt(bb.get(offset + 1)) << 8;
            case 1:
                k1 ^= (long) Byte.toUnsignedInt(bb.get(offset));
                break;
            case 0:
                break;
            default:
                throw new AssertionError("Should never get here.");
        }

        h1 ^= mixK1(k1);
        h2 ^= mixK2(k2);
    }

    @Override
    public HashCode hash() {
        return prepareHashCode(h1, h2, length);
    }

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

    private static long fmix64(long k) {
        k ^= k >>> 33;
        k *= 0xff51afd7ed558ccdL;
        k ^= k >>> 33;
        k *= 0xc4ceb9fe1a85ec53L;
        k ^= k >>> 33;
        return k;
    }

    private static long mixK1(long k1) {
        k1 *= C1;
        k1 = Long.rotateLeft(k1, 31);
        k1 *= C2;
        return k1;
    }

    private static long mixK2(long k2) {
        k2 *= C2;
        k2 = Long.rotateLeft(k2, 33);
        k2 *= C1;
        return k2;
    }

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
