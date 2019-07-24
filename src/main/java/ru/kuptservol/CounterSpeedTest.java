package ru.kuptservol;

import java.math.BigDecimal;
import java.util.concurrent.atomic.LongAdder;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static org.openjdk.jmh.annotations.Mode.AverageTime;

/**
 * @author Sergey Kuptsov
 * @since 12/06/2016
 * <p>
 * Results :
 * Benchmark                           Mode  Cnt      Score     Error  Units
 * CounterSpeedTest.bigDecimalCounter  avgt   30   3013,159 ±  75,594  us/op
 * CounterSpeedTest.doubleCounter      avgt   30   1066,382 ±  37,684  us/op
 * CounterSpeedTest.floatCounter       avgt   30   1047,050 ±  44,402  us/op
 * CounterSpeedTest.intCounter         avgt   30     42,632 ±   1,009  us/op
 * CounterSpeedTest.longAdderCounter   avgt   30  12412,276 ± 208,972  us/op
 * CounterSpeedTest.longCounter        avgt   30     58,328 ±   1,404  us/op
 */
@State(value = Scope.Benchmark)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
public class CounterSpeedTest {

    private static final int COUNT = 1000000;
    private double x_double = 1;
    private long x_long = 1;
    private int x_int = 1;
    private BigDecimal x_big_decimal = new BigDecimal(0);
    private float x_float = 1;
    private LongAdder longAdder = new LongAdder();


    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public LongAdder longAdderCounter() {
        for (int i = 0; i < COUNT; i++) {
            longAdder.increment();
        }
        return longAdder;
    }

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public double doubleCounter() {
        double tmp = 0;
        for (int i = 0; i < COUNT; i++) {
            tmp = x_double++;
        }
        return tmp;
    }

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public int intCounter() {
        int tmp = 0;
        for (int i = 0; i < COUNT; i++) {
            tmp = x_int++;
        }
        return tmp;
    }

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public long longCounter() {
        long tmp = 0;
        for (int i = 0; i < COUNT; i++) {
            tmp = x_long++;
        }
        return tmp;
    }

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public float floatCounter() {
        float tmp = 0;
        for (int i = 0; i < COUNT; i++) {
            tmp = x_float++;
        }

        return tmp;
    }

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public BigDecimal bigDecimalCounter() {
        BigDecimal add = new BigDecimal(0);
        for (int i = 0; i < COUNT; i++) {
            add = x_big_decimal.add(new BigDecimal(1));
        }

        return add;
    }
}
