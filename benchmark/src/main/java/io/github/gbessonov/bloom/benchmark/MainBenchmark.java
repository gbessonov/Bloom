package io.github.gbessonov.bloom.benchmark;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class MainBenchmark {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(MurmurHash3BenchmarkWithNew.class.getSimpleName())
                .include(MurmurHash3BenchmarkReuse.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
