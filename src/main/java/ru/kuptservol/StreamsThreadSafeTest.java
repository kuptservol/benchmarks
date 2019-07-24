package ru.kuptservol;

import java.util.stream.IntStream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static org.openjdk.jmh.annotations.Mode.AverageTime;
import static org.testng.Assert.assertEquals;

/**
 * @author Sergey Kuptsov
 * @since 18/01/2017
 */
@State(value = Scope.Benchmark)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
public class StreamsThreadSafeTest {

    private static final int COUNT = 1000000000;
    double i = 0;
    double j = 0;

    {
        for (int k = 0; k < COUNT; k++) {
            j++;
        }
    }

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public void testForeach() {
        i = 0;
        IntStream.range(0, COUNT).forEach(i -> {
            this.i++;
        });

        assertEquals(i, j);
    }
}
