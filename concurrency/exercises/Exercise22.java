package concurrency.exercises;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Exercise22 {
    static volatile boolean flag = false;

    static Object flagObject = new Object();

    static class BusyFirst implements Runnable {
        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    TimeUnit.SECONDS.sleep(1);
                    flag = true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class BusySecond implements Runnable {
        long lastCleared;
        long now;

        @Override
        public void run() {
            while(!Thread.interrupted()) {
                boolean shouldFinish = false;

                while(!flag) {
                    shouldFinish = Thread.interrupted();
                    if(shouldFinish) break;
                }

                if(flag) {
                    flag = false;
                    showReport();
                }
                if(shouldFinish) break;
            }
        }

        protected synchronized void showReport() {
            now = System.currentTimeMillis();
            System.out.format("Clear flag ( delay=%s )%n", (now - lastCleared) / 1000.0);
            lastCleared = now;
        }
    }

    static class WaitFirst implements Runnable {
        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    TimeUnit.SECONDS.sleep(1);
                    synchronized (flagObject) {
                        flagObject.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class WaitSecond extends BusySecond {
        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    synchronized (flagObject) {
                        flagObject.wait();
                        showReport();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new BusyFirst());
        exec.execute(new BusySecond());

        stopExec(exec);

        System.out.println("Using Wait...");
        exec = Executors.newCachedThreadPool();
        exec.execute(new WaitFirst());
        exec.execute(new WaitSecond());

        stopExec(exec);
    }

    private static void stopExec(ExecutorService exec) throws InterruptedException {
        exec.shutdown();
        if(!exec.awaitTermination(5, TimeUnit.SECONDS)){
            System.out.println("Terminating manually...");
            exec.shutdownNow();
        }
    }
}
