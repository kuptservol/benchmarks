package ru.kuptservol;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Kuptsov
 * @since 20/01/2017
 */
public class BatchWithTimeoutOnLocks extends BaseBatchWithTimeout {

    private final static BlockingQueue<Integer> eventsQueue = new ArrayBlockingQueue<>(PUSH_QUEUE_MAX_SIZE);

    private final static ReentrantLock lock = new ReentrantLock();

    private final static Condition enoughElement = lock.newCondition();

    public BatchWithTimeoutOnLocks(CountDownLatch latch) {
        doStart(latch);
    }

    public static void handlePushEvent(Integer pushEvent) {
        lock.lock();
        try {
            eventsQueue.offer(pushEvent);
            if (eventsQueue.size() >= PUSH_BATCH_SIZE) {
                enoughElement.signal();
            }
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }

    protected static void doStart(CountDownLatch latch) {
        for (int i = 0; i < N_THREADS; i++) {
            executor.execute(
                    new PushEventsQueueProcessorTask(
                            eventsQueue,
                            lock,
                            enoughElement,
                            latch
                    )
            );
        }
    }

    @Override
    protected void publishEvent(int i) {
        handlePushEvent(i);
    }

    private static class PushEventsQueueProcessorTask implements Runnable {

        private final BlockingQueue<Integer> pushEventsQueue;

        private final ReentrantLock lock;
        private final Condition enoughElement;
        private CountDownLatch latch;

        private PushEventsQueueProcessorTask(
                BlockingQueue<Integer> pushEventsQueue,
                ReentrantLock lock,
                Condition enoughElement,
                CountDownLatch latch)
        {
            this.pushEventsQueue = pushEventsQueue;
            this.enoughElement = enoughElement;
            this.lock = lock;
            this.latch = latch;
        }

        @Override
        public void run() {
            while (true) {
                List<Integer> pushEvents = new ArrayList<>(PUSH_BATCH_SIZE);

                lock.lock();

                try {
                    if (pushEventsQueue.size() < PUSH_BATCH_SIZE) {
                        enoughElement.await(PUSH_PERIOD, PUSH_PERIOD_TIMEUNIT);
                    }

                    pushEventsQueue.drainTo(pushEvents, PUSH_BATCH_SIZE);

                    if (pushEvents.isEmpty()) {
                        continue;
                    }
                } catch (Exception e) {
                    break;
                } finally {
                    lock.unlock();
                }

                subscribe(latch, pushEvents);
            }
        }
    }


}
