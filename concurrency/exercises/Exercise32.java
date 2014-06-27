package concurrency.exercises;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static net.mindview.util.Print.print;

public class Exercise32 {
    static class Count {
        private int count = 0;
        private Random rand = new Random(47);
        // Remove the synchronized keyword to see counting fail:
        public synchronized int increment() {
            int temp = count;
            if(rand.nextBoolean()) // Yield half the time
                Thread.yield();
            return (count = ++temp);
        }
        public synchronized int value() { return count; }
    }

    static class Entrance implements Runnable {
        private static Count count = new Count();
        private static List<Entrance> entrances =
                new ArrayList<Entrance>();
        private final CountDownLatch latch;
        private int number = 0;
        // Doesn't need synchronization to read:
        private final int id;
        private static volatile boolean canceled = false;
        // Atomic operation on a volatile field:
        public static void cancel() { canceled = true; }
        public Entrance(int id, CountDownLatch latch) {
            this.id = id;
            this.latch = latch;
            // Keep this task in a list. Also prevents
            // garbage collection of dead tasks:
            entrances.add(this);
        }
        public void run() {
            while(!canceled) {
                synchronized(this) {
                    ++number;
                }
                print(this + " Total: " + count.increment());
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch(InterruptedException e) {
                    print("sleep interrupted");
                }
            }
            print("Stopping " + this);
            latch.countDown();
        }
        public synchronized int getValue() { return number; }
        public String toString() {
            return "Entrance " + id + ": " + getValue();
        }
        public static int getTotalCount() {
            return count.value();
        }
        public static int sumEntrances() {
            int sum = 0;
            for(Entrance entrance : entrances)
                sum += entrance.getValue();
            return sum;
        }
    }

    static class OrnamentalGarden {
        public static int SIZE = 5;

        public static void main(String[] args) throws Exception {
            ExecutorService exec = Executors.newCachedThreadPool();

            CountDownLatch latch = new CountDownLatch(SIZE);

            for(int i = 0; i < SIZE; i++)
                exec.execute(new Entrance(i, latch));
            // Run for a while, then stop and collect the data:
            TimeUnit.SECONDS.sleep(3);
            Entrance.cancel();

            if(!latch.await(250, TimeUnit.MILLISECONDS))
                print("Some tasks were not terminated!");

            print("Total: " + Entrance.getTotalCount());
            print("Sum of Entrances: " + Entrance.sumEntrances());
        }
    }

    public static void main(String[] args) throws Exception {
        OrnamentalGarden.main(args);
    }
}
