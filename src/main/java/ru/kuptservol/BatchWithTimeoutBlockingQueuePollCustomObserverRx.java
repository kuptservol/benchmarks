package ru.kuptservol;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.ForwardingQueue;
import rx.Observable;
import rx.schedulers.Schedulers;

import static java.util.concurrent.TimeUnit.MICROSECONDS;

/**
 * @author Sergey Kuptsov
 * @since 20/01/2017
 */
public class BatchWithTimeoutBlockingQueuePollCustomObserverRx extends BaseBatchWithTimeout {

    private final static ForwardingQueue<Integer> eventsQueue = EvictingQueue.create(PUSH_QUEUE_MAX_SIZE);

    public BatchWithTimeoutBlockingQueuePollCustomObserverRx(CountDownLatch latch) {
        Observable<List<Integer>> pushesToSend = Observable.<Integer>create(
                observer -> {
                    Integer pushEvent = eventsQueue.poll();
                    if (pushEvent != null) {
                        observer.onNext(pushEvent);
                    }
                    observer.onCompleted();
                })
                .repeatWhen(observable -> observable
                        .delay(1, MICROSECONDS)
                        .observeOn(Schedulers.from(executor)))
                .buffer(PUSH_PERIOD,
                        PUSH_PERIOD_TIMEUNIT,
                        PUSH_BATCH_SIZE)
                .flatMap(data -> Observable.just(data)
                        .subscribeOn(Schedulers.from(executor))
                        .doOnNext(s -> {
                            if (s.size() == 0) {
                                return;
                            }
                            subscribe(latch, s);
                        })
                );

        pushesToSend.subscribe();
    }

    @Override
    protected void publishEvent(int i) {
        eventsQueue.offer(i);
    }
}
