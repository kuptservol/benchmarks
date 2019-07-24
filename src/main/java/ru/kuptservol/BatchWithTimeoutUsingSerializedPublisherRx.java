package ru.kuptservol;

import java.util.concurrent.CountDownLatch;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * @author Sergey Kuptsov
 * @since 20/01/2017
 */
public class BatchWithTimeoutUsingSerializedPublisherRx extends BaseBatchWithTimeout {

    Subject<Integer, Integer> eventsSubject = PublishSubject.<Integer>create();

    public BatchWithTimeoutUsingSerializedPublisherRx(CountDownLatch latch) {
        SerializedSubject<Integer, Integer> integerIntegerSerializedSubject = eventsSubject.toSerialized();

        integerIntegerSerializedSubject
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
                )
                .doOnCompleted(() -> System.out.println("System shuts down"))
                .subscribe();
    }

    @Override
    protected void publishEvent(int i) {
        eventsSubject.onNext(i);
    }
}
