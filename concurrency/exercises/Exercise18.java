package concurrency.exercises;

import java.util.concurrent.TimeUnit;

public class Exercise18 {


    static class NonTask {
        void longOperation() throws InterruptedException {
            TimeUnit.DAYS.sleep(365);
        }
    }

    static class Task implements Runnable {
        NonTask nonTask = new NonTask();

        volatile boolean isValidTermination = false;

        synchronized boolean isValidTermination() { return isValidTermination;}

        @Override
        public void run() {
            try {
                nonTask.longOperation();
            } catch (InterruptedException e) {
                isValidTermination = true;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Task task = new Task();
        Thread thread = new Thread(task);

        thread.start();
        thread.interrupt();

        while(thread.isAlive())
            Thread.sleep(10);

        System.out.println("is valid termination: " + task.isValidTermination());
    }

}
