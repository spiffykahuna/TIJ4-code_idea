package concurrency.exercises;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Exercise21 {
    static class First implements Runnable {
        @Override
        public void run() {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("MESSAGE from " + this);
        }
    }

    static class Second implements Runnable {
        private final Runnable first;

        Second(Runnable first) {
            this.first = first;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (first) {
                first.notifyAll();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        First first = new First();
        Second second = new Second(first);
        exec.execute(first);
        exec.execute(second);

        exec.shutdown();
        exec.awaitTermination(3, TimeUnit.SECONDS);
    }
}
