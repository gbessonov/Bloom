/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.github.gbessonov.bloom.benchmark;

import com.google.common.hash.Hashing;
import io.github.gbessonov.bloom.hashing.Murmur3f;
import org.greenrobot.essentials.hash.Murmur3F;
import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class MurmurHash3BenchmarkReuse {

    private byte[] data;

    private final Murmur3f ownHasher = new Murmur3f();
    private final Murmur3F greenRobotHasher = new Murmur3F();

    @Setup
    public void setup() {
        data = new byte[1024];
        new Random().nextBytes(data);
    }

    @Benchmark
    public byte[] benchmarkMurmurHash3() {
        ownHasher.reset(0);
        return ownHasher.include(data).hash().getValueBytesLittleEndian();
    }

    @Benchmark
    public byte[] benchmarkMurmurHash3Guava() {
        return Hashing.murmur3_128().hashBytes(data).asBytes();
    }

    @Benchmark
    public byte[] benchmarkMurmurHash3GreenRobot() {
        greenRobotHasher.reset();
        greenRobotHasher.update(data);
        return greenRobotHasher.getValueBytesLittleEndian();
    }
}
