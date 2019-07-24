package ru.kuptservol;

import java.util.TimeZone;
import java.util.stream.IntStream;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static org.joda.time.DateTimeZone.UTC;
import static org.openjdk.jmh.annotations.Mode.AverageTime;

/**
 * @author Sergey Kuptsov
 * @since 01/12/2016
 */
@State(value = Scope.Benchmark)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
public class TimeZoneBench {

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public int jodaTimeZone() {
        return IntStream.range(0, 4)
                .parallel()
                .map(j -> {
                            int offset = 0;
                            for (int i = 0; i < 1000; i++) {
                                DateTimeZone dateTimeZone = DateTimeZone.forID("Europe/Moscow");
                                DateTime instant = new DateTime(DateTime.now(UTC));

                                offset += dateTimeZone.getOffset(instant) / (1000 * 3600);
                            }
                            return offset;
                        }
                )
                .reduce((a, b) -> a + b)
                .getAsInt();
    }

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public double javaTimeZone() {
        return IntStream.range(0, 4)
                .parallel()
                .map(j -> {
                            int offset = 0;
                            for (int i = 0; i < 1000; i++) {
                                TimeZone timeZone = TimeZone.getTimeZone("Europe/Moscow");
                                DateTime instant = new DateTime(DateTime.now(UTC));

                                offset += timeZone.getOffset(instant.getMillis());
                            }
                            return offset;
                        }
                )
                .reduce((a, b) -> a + b)
                .getAsInt();
    }
}
