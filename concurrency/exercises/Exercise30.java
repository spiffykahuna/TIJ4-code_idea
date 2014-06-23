package concurrency.exercises;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;

import static net.mindview.util.Print.print;
import static net.mindview.util.Print.printnb;

public class Exercise30 {
    static class Sender implements Runnable {
        private Random rand = new Random(47);
        private BlockingQueue<Character> out = new LinkedBlockingQueue<Character>(10);

        public BlockingQueue<Character> getCharacterQueue() { return out; }

        public void run() {
            try {
                while(true)
                    for(char c = 'A'; c <= 'z'; c++) {
                        out.put(c);
                        TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));
                    }
            } catch(InterruptedException e) {
                print(e + " Sender sleep interrupted");
            }
        }
    }

    static class Receiver implements Runnable {
        private BlockingQueue<Character> in;
        public Receiver(Sender sender) throws IOException {
            in = sender.getCharacterQueue();
        }
        public void run() {
            try {
                while(true) {
                    // Blocks until characters are there:
                    printnb("Read: " + in.take() + ", ");
                }
            } catch(InterruptedException e) {
                print(e + " Receiver read exception");
            }
        }
    }

    static class PipedIO {
        public static void main(String[] args) throws Exception {
            Sender sender = new Sender();
            Receiver receiver = new Receiver(sender);
            ExecutorService exec = Executors.newCachedThreadPool();
            exec.execute(sender);
            exec.execute(receiver);
            TimeUnit.SECONDS.sleep(4);
            exec.shutdownNow();
        }
    }

    public static void main(String[] args) throws Exception {
        PipedIO.main(args);
    }
}
