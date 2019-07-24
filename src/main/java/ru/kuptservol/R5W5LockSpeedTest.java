package ru.kuptservol;

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
 * @since 25/01/2017
 */
@State(value = Scope.Benchmark)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
/**
 * Benchmark                                                Mode  Cnt     Score      Error  Units
 R5W5LockSpeedTest.rWReentrantRWLockSpeedTest             avgt   10  7730,744 ± 2296,881  us/op
 R5W5LockSpeedTest.stampedPessimisticLockRWLockSpeedTest  avgt   10  2854,864 ±  292,565  us/op
 R5W5LockSpeedTest.syncRWLockSpeedTest                    avgt   10  3513,406 ±  233,044  us/op
 */
public class R5W5LockSpeedTest extends BaseRWLockSpeedTest {

    public R5W5LockSpeedTest() {
        super(5, 5);
    }

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public void stampedPessimisticLockRWLockSpeedTest() {
        super.stampedPessimisticLockRWLockSpeedTest();
    }

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public void syncRWLockSpeedTest() {
        super.syncRWLockSpeedTest();
    }

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public void rWReentrantRWLockSpeedTest() {
        super.rWReentrantRWLockSpeedTest();
    }
}
