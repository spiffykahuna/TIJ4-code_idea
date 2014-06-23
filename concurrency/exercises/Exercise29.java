package concurrency.exercises;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static net.mindview.util.Print.print;

public class Exercise29 {
    static class Toast {

        public enum Status { DRY, BUTTERED, JAMMED, PEANUT_BUTTERED, JELLIED }
        private Status status = Status.DRY;
        private final int id;
        public Toast(int idn) { id = idn; }

        public void butter() { status = Status.BUTTERED; }
        public void jam() { status = Status.JAMMED; }
        public void peanutButter() { status = Status.PEANUT_BUTTERED; }
        public void jelly() { status = Status.JELLIED; }

        public Status getStatus() { return status; }
        public int getId() { return id; }
        public String toString() {
            return "Toast " + id + ": " + status;
        }
    }

    static class ToastQueue extends LinkedBlockingQueue<Toast> {}

    static abstract class ToastMaker implements Runnable {
        ToastQueue inputQueue;
        ToastQueue outputQueue;

        public ToastMaker(ToastQueue inputQueue, ToastQueue outputQueue) {
            this.inputQueue = inputQueue;
            this.outputQueue = outputQueue;
        }

        public void run() {
            try {
                while(!Thread.interrupted()) {
                    putToast(
                        makeToast(
                            getToast()
                        )
                    );
                }
            } catch(InterruptedException e) {
                print(this.getClass().getSimpleName() + " interrupted");
            }
            print(this.getClass().getSimpleName() + " off");
        }

        protected Toast getToast() throws InterruptedException {
            return inputQueue.take();
        }

        protected abstract Toast makeToast(Toast toast) throws InterruptedException;

        protected void putToast(Toast toast) throws InterruptedException {
            outputQueue.put(toast);
        }
    }

    static class Toaster extends ToastMaker {
        private int count = 0;
        private Random rand = new Random(47);

        protected Toaster(ToastQueue inputQueue, ToastQueue outputQueue) {
            super(inputQueue, outputQueue);
        }

        @Override
        protected Toast getToast() { return null; }

        @Override
        protected Toast makeToast(Toast toast) throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(
                    100 + rand.nextInt(500));
            // Make toast
            Toast t = new Toast(count++);
            print(t);
            return t;
        }
    }

    // Apply butter to toast:
    static class Butterer extends ToastMaker {
        public Butterer(ToastQueue inputQueue, ToastQueue outputQueue) {
            super(inputQueue, outputQueue);
        }

        @Override
        protected Toast makeToast(Toast toast) throws InterruptedException {
            toast.butter();
            print(toast);
            return toast;
        }
    }

    // Apply jam to buttered toast:
    static class Jammer  extends ToastMaker{

        public Jammer(ToastQueue inputQueue, ToastQueue outputQueue) {
            super(inputQueue, outputQueue);
        }

        @Override
        protected Toast makeToast(Toast toast) throws InterruptedException {
            toast.jam();
            print(toast);
            return toast;
        }
    }

    // Consume the toast:
    static class Eater extends ToastMaker {
        private int counter = 0;
        public Eater(ToastQueue finished) {
            super(finished, new ToastQueue());
        }

        @Override
        protected Toast makeToast(Toast t) throws InterruptedException {
            if(t.getId() != counter++ ||
                    !hasProperStatus(t)) {
                print(">>>> Error: " + t);
                System.exit(1);
            } else
                print("Chomp! " + t);
            return t;
        }

        private boolean hasProperStatus(Toast t) {
            Toast.Status status = t.getStatus();
            return (status == Toast.Status.PEANUT_BUTTERED) || (status == Toast.Status.JELLIED);
        }
    }

    static class PeanutButterer  extends ToastMaker{

        public PeanutButterer(ToastQueue inputQueue, ToastQueue outputQueue) {
            super(inputQueue, outputQueue);
        }

        @Override
        protected Toast makeToast(Toast toast) throws InterruptedException {
            toast.peanutButter();
            print(toast);
            return toast;
        }
    }

    static class Jellier  extends ToastMaker{

        public Jellier(ToastQueue inputQueue, ToastQueue outputQueue) {
            super(inputQueue, outputQueue);
        }

        @Override
        protected Toast makeToast(Toast toast) throws InterruptedException {
            toast.jelly();
            print(toast);
            return toast;
        }
    }


    static class ToastOMatic {
        public static void main(String[] args) throws Exception {
            ToastQueue
                    dummyQueue = new ToastQueue(),
                    dryQueue = new ToastQueue(),
                    butteredQueue = new ToastQueue(),
                    jammedQueue = new ToastQueue(),
                    finishedQueue = new ToastQueue();

            ExecutorService exec = Executors.newCachedThreadPool();

            exec.execute(new Toaster(dummyQueue,dryQueue));
            exec.execute(new Butterer(dryQueue, butteredQueue));
            exec.execute(new Jammer(butteredQueue, jammedQueue));

            exec.execute(new PeanutButterer(jammedQueue, finishedQueue));
            exec.execute(new Jellier(jammedQueue, finishedQueue));

            exec.execute(new Eater(finishedQueue));

            TimeUnit.SECONDS.sleep(5);

            exec.shutdownNow();
        }
    }

    public static void main(String[] args) throws Exception {
        ToastOMatic.main(args);
    }
}
