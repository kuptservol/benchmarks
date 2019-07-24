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
 * Benchmark                                                 Mode  Cnt        Score         Error  Units
 R19W1LockSpeedTest.rWReentrantRWLockSpeedTest             avgt   10  2415087,031 ± 2669636,447  us/op
 R19W1LockSpeedTest.stampedOptimisticLockRWLockSpeedTest   avgt   10   471373,962 ±  744752,311  us/op
 R19W1LockSpeedTest.stampedPessimisticLockRWLockSpeedTest  avgt   10    58546,341 ±   19897,359  us/op
 R19W1LockSpeedTest.syncRWLockSpeedTest                    avgt   10    99922,651 ±    8859,416  us/op
 */
public class R19W1LockSpeedTest extends BaseRWLockSpeedTest {

    public R19W1LockSpeedTest() {
        super(19, 1);
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

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public void stampedOptimisticLockRWLockSpeedTest() {
        super.stampedOptimisticLockRWLockSpeedTest();
    }
}
