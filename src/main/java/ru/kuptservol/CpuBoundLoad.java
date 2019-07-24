package ru.kuptservol;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

/**
 * @author Sergey Kuptsov
 * @since 25/11/2016
 */
public class CpuBoundLoad {

    private static class HeavyThread implements Runnable {

        private long length;

        public HeavyThread(long length) {
            this.length = length;
            new Thread(this).start();
        }

        @Override
        public void run() {
            while (true) {
                String data = "";

                // make some stuff up
                for (int i = 0; i < length; i++) {
                    data += UUID.randomUUID().toString();
                }

                MessageDigest digest;
                try {
                    digest = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }

                // hash the data
                digest.update(data.getBytes());
            }
        }
    }


    /**
     * thread that does little work. just count & sleep
     *
     * @author srasul
     */
    private static class LightThread implements Runnable {

        public LightThread() {
            new Thread(this).start();
        }

        public static void main(String[] args) {
            // lets start 1 heavy ...
            new HeavyThread(1000);

            // ... and 3 light threads
            new LightThread();
            new LightThread();
            new LightThread();
        }

        @Override
        public void run() {
            Long l = 0l;
            while (true) {
                l++;
                try {
                    Thread.sleep(new Random().nextInt(10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (l == Long.MAX_VALUE) {
                    l = 0l;
                }
            }
        }
    }
}
