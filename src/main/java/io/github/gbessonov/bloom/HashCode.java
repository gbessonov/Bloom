package io.github.gbessonov.bloom;

import java.math.BigInteger;

public interface HashCode {
    byte[] getValueBytesBigEndian();
    byte[] getValueBytesLittleEndian();
    BigInteger getValueBigInteger();
    String getValueHexString();
}
