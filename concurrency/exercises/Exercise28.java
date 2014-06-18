package concurrency.exercises;

import concurrency.LiftOff;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import static net.mindview.util.Print.print;

public class Exercise28 {
    static class LiftOffRunner implements Runnable {
        private BlockingQueue<LiftOff> rockets;
        public LiftOffRunner(BlockingQueue<LiftOff> queue) {
            rockets = queue;
        }
        public void add(LiftOff lo) {
            try {
                rockets.put(lo);
            } catch(InterruptedException e) {
                print("Interrupted during put()");
            }
        }
        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    LiftOff rocket = rockets.take();
                    rocket.run(); // Use this thread
                }
            } catch(InterruptedException e) {
                print("Waking from take()");
            }
            print("Exiting LiftOffRunner");
        }
    }

    static class  LiftOffCreator implements Runnable {
        private BlockingQueue<LiftOff> rockets;
        public LiftOffCreator(BlockingQueue<LiftOff> queue) {
            rockets = queue;
        }
        @Override
        public void run() {
            try {
                for(int i = 0; i < 5; i++)
                    rockets.put(new LiftOff(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class TestBlockingQueues {
        static void getkey() {
            try {
                // Compensate for Windows/Linux difference in the
                // length of the result produced by the Enter key:
                new BufferedReader(
                        new InputStreamReader(System.in)).readLine();
            } catch(java.io.IOException e) {
                throw new RuntimeException(e);
            }
        }

        static void getkey(String message) {
            print(message);
            getkey();
        }

        static void
        test(String msg, BlockingQueue<LiftOff> queue) {
            print(msg);
            LiftOffRunner runner = new LiftOffRunner(queue);
            Thread t = new Thread(runner);
            t.start();
            Thread creatorThread = new Thread(new LiftOffCreator(queue));
            creatorThread.start();
            getkey("Press 'Enter' (" + msg + ")");
            t.interrupt();
            creatorThread.interrupt();
            print("Finished " + msg + " test");
        }

        public static void main(String[] args) {
            test("LinkedBlockingQueue", // Unlimited size
                    new LinkedBlockingQueue<LiftOff>());
            test("ArrayBlockingQueue", // Fixed size
                    new ArrayBlockingQueue<LiftOff>(3));
            test("SynchronousQueue", // Size of 1
                    new SynchronousQueue<LiftOff>());
        }
    }

    public static void main(String[] args) {
        TestBlockingQueues.main(args);
    }
}
