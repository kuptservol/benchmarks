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
 * Benchmark                                                 Mode  Cnt       Score        Error  Units
 R16W4LockSpeedTest.rWReentrantRWLockSpeedTest             avgt   10  710868,145 ± 640773,937  us/op
 R16W4LockSpeedTest.stampedPessimisticLockRWLockSpeedTest  avgt   10    9700,286 ±   1095,906  us/op
 R16W4LockSpeedTest.syncRWLockSpeedTest                    avgt   10   24957,747 ±   2984,698  us/op
 */
public class R16W4LockSpeedTest extends BaseRWLockSpeedTest {

    public R16W4LockSpeedTest() {
        super(16, 4);
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
