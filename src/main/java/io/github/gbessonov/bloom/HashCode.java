package io.github.gbessonov.bloom;

import java.math.BigInteger;

/**
 * Represents a 128-bit hash code produced by a {@link HashFunction}.
 * <p>
 * Implementations of this interface provide access to the hash in multiple formats
 * for serialization, comparison, or integration with other systems.
 */
public interface HashCode {

    /**
     * Returns the hash code as a 16-byte array in big-endian order.
     * <p>
     * The most significant byte comes first. This format is typically used for
     * interoperability with cryptographic or network-oriented protocols.
     *
     * @return a 16-byte array representing the hash in big-endian format
     */
    byte[] getValueBytesBigEndian();

    /**
     * Returns the hash code as a 16-byte array in little-endian order.
     * <p>
     * The least significant byte comes first. This is often more efficient on
     * little-endian platforms and is the native format of Murmur3.
     *
     * @return a 16-byte array representing the hash in little-endian format
     */
    byte[] getValueBytesLittleEndian();

    /**
     * Returns the hash code as a positive {@link BigInteger} in big-endian order.
     * <p>
     * This representation is useful for numeric comparisons or conversions to
     * other integer-based formats. The sign is always positive.
     *
     * @return a positive {@link BigInteger} representation of the hash
     */
    BigInteger getValueBigInteger();

    /**
     * Returns the hash code as a lowercase hexadecimal string.
     * <p>
     * The result is 32 hexadecimal characters (128 bits) with leading zero padding
     * if necessary.
     *
     * @return a 32-character lowercase hexadecimal string representation of the hash
     */
    String getValueHexString();
}
