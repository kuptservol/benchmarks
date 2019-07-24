package ru.kuptservol;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * @author Sergey Kuptsov
 * @since 20/01/2017
 */
public abstract class BaseBatchWithTimeout {

    public static final int N_THREADS = 10;
    public final static ExecutorService executor = newFixedThreadPool(
            N_THREADS,
            new ThreadFactoryBuilder().setDaemon(true).setNameFormat("PushClientQueueProcessor-%d").build()
    );

    public final static ExecutorService eventPublisher = newFixedThreadPool(
            N_THREADS,
            new ThreadFactoryBuilder().setDaemon(true).setNameFormat("EventPublisherProcessor-%d").build()
    );
    public static final long PUSH_PERIOD = 100;
    public static final int EVENTS_COUNT = 100000;
    public static final int PUSH_QUEUE_MAX_SIZE = EVENTS_COUNT;
    public static final int PUSH_BATCH_SIZE = 10;

    public static final TimeUnit PUSH_PERIOD_TIMEUNIT = TimeUnit.MILLISECONDS;

    protected static void subscribe(CountDownLatch latch, List<Integer> s) {
//        System.out.println(s);
        try {
            Thread.sleep(10);
//            System.out.println("Heavy thinking started");
//            Thread.sleep(100000);
//            System.out.println("Heavy thinking finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < s.size(); i++) {
            latch.countDown();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(EVENTS_COUNT);
//        BaseBatchWithTimeout baseBatchWithTimeout = new BatchWithTimeoutOnLocks(latch);
        BaseBatchWithTimeout baseBatchWithTimeout = new BatchWithTimeoutBlockingQueuePollCustomObserverRx(latch);
//        BaseBatchWithTimeout baseBatchWithTimeout = new BatchWithTimeoutUsingSerializedPublisherRx(latch);
        long time = System.nanoTime();
        for (int i = 0; i < EVENTS_COUNT; i++) {
            final int j = i;
            CompletableFuture.runAsync(
                    () -> baseBatchWithTimeout.publishEvent(j),
                    eventPublisher);

        }

//        System.out.println("Publish single event");
//        baseBatchWithTimeout.publishEvent(0);
//        System.out.println("Single event published");

        latch.await();

        System.out.println("Time : " + (System.nanoTime() - time) / (1000 * 1000) + " ms");
    }

    protected abstract void publishEvent(int i);
}
