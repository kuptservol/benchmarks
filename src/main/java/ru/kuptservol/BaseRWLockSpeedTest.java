package ru.kuptservol;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * @author Sergey Kuptsov
 * @since 25/01/2017
 */
public class BaseRWLockSpeedTest {

    protected final int readersNum;
    protected final int writersNum;

    public BaseRWLockSpeedTest(int readersNum, int writersNum) {
        this.readersNum = readersNum;
        this.writersNum = writersNum;
    }

    protected void syncRWLockSpeedTest() {
        SyncRWLockSpeedTest syncRWLockSpeedTest = new SyncRWLockSpeedTest(readersNum, writersNum);
        syncRWLockSpeedTest.run();
    }

    protected void rWReentrantRWLockSpeedTest() {
        RWReentrantRWLockSpeedTest rWReentrantRWLockSpeedTest = new RWReentrantRWLockSpeedTest(readersNum, writersNum);
        rWReentrantRWLockSpeedTest.run();
    }

    protected void stampedPessimisticLockRWLockSpeedTest() {
        StampedPessimisticLockRWLockSpeedTest stampedPessimisticLockRWLockSpeedTest = new StampedPessimisticLockRWLockSpeedTest(readersNum, writersNum);
        stampedPessimisticLockRWLockSpeedTest.run();
    }

    protected void stampedOptimisticLockRWLockSpeedTest() {
        StampedOptimisticLockRWLockSpeedTest StampedOptimisticLockRWLockSpeedTest = new StampedOptimisticLockRWLockSpeedTest(readersNum, writersNum);
        StampedOptimisticLockRWLockSpeedTest.run();
    }

    public abstract static class BaseRWLockSpeed implements Runnable {
        protected final int N = 100000;
        protected final int readersNum;
        protected final int writersNum;
        protected final ExecutorService readers;
        protected final ExecutorService writers;
        protected long i = 0;

        protected BaseRWLockSpeed(int readersNum, int writersNum) {
            this.readersNum = readersNum;
            this.writersNum = writersNum;
            readers = Executors.newFixedThreadPool(readersNum);
            writers = Executors.newFixedThreadPool(writersNum);
        }

        @Override
        public void run() {
            for (int j = 0; j < writersNum; j++) {
                writers.submit(this::write_);
            }

            for (int j = 0; j < readersNum; j++) {
                readers.submit(this::read_);
            }

            writers.shutdown();
            readers.shutdown();

            try {
                writers.awaitTermination(3, TimeUnit.SECONDS);
                readers.awaitTermination(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                System.out.println("Termination failed");
            }
        }

        protected void read_() {
            while (i < N) {
                read();
            }
        }

        protected void write_() {
            while (i < N) {
                write();
            }
        }

        protected abstract long read();

        protected abstract void write();
    }

    public static class SyncRWLockSpeedTest extends BaseRWLockSpeed {

        private final Object lock = new Object();

        protected SyncRWLockSpeedTest(int readersNum, int writersNum) {
            super(readersNum, writersNum);
        }

        @Override
        protected long read() {
            synchronized (lock) {
                return i;
            }
        }

        @Override
        protected void write() {
            synchronized (lock) {
                i++;
            }
        }
    }

    public static class RWReentrantRWLockSpeedTest extends BaseRWLockSpeed {

        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        protected RWReentrantRWLockSpeedTest(int readersNum, int writersNum) {
            super(readersNum, writersNum);
        }

        @Override
        protected long read() {
            lock.readLock().lock();
            try {
                return i;
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        protected void write() {
            lock.writeLock().lock();
            try {
                i++;
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    public static class StampedPessimisticLockRWLockSpeedTest extends BaseRWLockSpeed {

        private final StampedLock lock = new StampedLock();

        protected StampedPessimisticLockRWLockSpeedTest(int readersNum, int writersNum) {
            super(readersNum, writersNum);
        }

        @Override
        protected long read() {
            long stamp = lock.readLock();
            try {
                return i;
            } finally {
                lock.unlock(stamp);
            }
        }

        @Override
        protected void write() {
            long stamp = lock.writeLock();
            try {
                i++;
            } finally {
                lock.unlock(stamp);
            }
        }
    }

    public static class StampedOptimisticLockRWLockSpeedTest extends BaseRWLockSpeed {

        private final StampedLock lock = new StampedLock();

        protected StampedOptimisticLockRWLockSpeedTest(int readersNum, int writersNum) {
            super(readersNum, writersNum);
        }

        @Override
        protected long read() {
            long optimisticStamp = lock.tryOptimisticRead();
            long y = i;

            if (!lock.validate(optimisticStamp)) {
                long stamp = lock.readLock();
                try {
                    return i;
                } finally {
                    lock.unlock(stamp);
                }
            }

            return y;
        }

        @Override
        protected void write() {
            long stamp = lock.writeLock();
            try {
                i++;
            } finally {
                lock.unlock(stamp);
            }
        }
    }
}
