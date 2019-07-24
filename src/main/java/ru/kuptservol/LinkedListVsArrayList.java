package ru.kuptservol;

import java.util.ArrayList;
import java.util.LinkedList;

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
 *         <p>
 *         Benchmark                         Mode  Cnt     Score     Error  Units
 *         LinkedListVsArrayList.arrayList   avgt   10   714,407 ± 482,997  us/op
 *         LinkedListVsArrayList.linkedList  avgt   10  1402,569 ±  93,083  us/op
 */
@State(value = Scope.Benchmark)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
public class LinkedListVsArrayList {

    LinkedList<Integer> linkedList = new LinkedList<>();
    ArrayList<Integer> arrayList = new ArrayList<>();

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public int linkedList()
    {
        int i;
        for (i = 0; i < 1000; i++) {
            linkedList.add(i);
        }

        return i;
    }

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public int arrayList()
    {
        int i;
        for (i = 0; i < 1000; i++) {
            arrayList.add(i);
        }

        return i;
    }
}
