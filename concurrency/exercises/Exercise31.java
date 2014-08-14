package concurrency.exercises;

import concurrency.Chopstick;
import concurrency.Philosopher;

import java.util.Random;
import java.util.concurrent.*;

import static net.mindview.util.Print.print;

public class Exercise31 {
    static class Chopstick {
        private boolean taken = false;
        public synchronized void take() throws InterruptedException {
            while(taken)
                wait();
            taken = true;
        }
        public synchronized void drop() {
            taken = false;
            notifyAll();
        }
    }

    static class Bin extends LinkedBlockingQueue<Chopstick> {
        public Bin(int size) {
            super(size);
        }
    }

    static class Philosopher implements Runnable {
        private Chopstick left;
        private Chopstick right;

        private Bin bin;

        private final int id;
        private final int ponderFactor;
        private Random rand = new Random(47);
        private void pause() throws InterruptedException {
            if(ponderFactor == 0) return;
            TimeUnit.MILLISECONDS.sleep(
                    rand.nextInt(ponderFactor * 250));
        }
        public Philosopher(Bin bin,
                           int ident, int ponder) {
            this.bin = bin;
            id = ident;
            ponderFactor = ponder;
        }
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    print(this + " " + "thinking");
                    pause();
                    // Philosopher becomes hungry
                    take();
                    print(this + " " + "eating");
                    pause();
                    give();
                }
            } catch(InterruptedException e) {
                print(this + " " + "exiting via interrupt");
            }
        }

        private void take() throws InterruptedException {
            print(this + " " + "grabbing right");
            right = bin.take();
            print(this + " " + "grabbing left");
            left = bin.take();
        }

        private void give() throws InterruptedException {
            bin.put(right);
            right = null;

            bin.put(left);
            left = null;
        }

        public String toString() { return "Philosopher " + id; }
    }

    static class DeadlockingDiningPhilosophers {
        public static void main(String[] args) throws Exception {
            ExecutorService exec = Executors.newCachedThreadPool();

            int ponder = 5;
            if(args.length > 0) {
                ponder = Integer.parseInt(args[0]);
            }
            int size = 5;
            if(args.length > 1) {
                size = Integer.parseInt(args[1]);
            }

            int binSize = size;
            if(args.length > 2) {
                binSize = Integer.parseInt(args[2]);
            }

            Bin bin = new Bin(binSize);

            for(int i = 0; i < size; i++)
                bin.put(new Chopstick());

            for(int i = 0; i < size; i++)
                exec.execute(new Philosopher(bin, i, ponder));

            if(args.length == 4 && args[3].equals("timeout"))
                TimeUnit.SECONDS.sleep(5);
            else {
                System.out.println("Press 'Enter' to quit");
                System.in.read();
            }
            exec.shutdownNow();
        }
    }

    public static void main(String[] args) throws Exception {
        DeadlockingDiningPhilosophers.main(new String[] {"1", "30", "30"});
    }
}
