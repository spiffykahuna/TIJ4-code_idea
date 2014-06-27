package concurrency.exercises;

import concurrency.Fat;
import net.mindview.util.BasicGenerator;
import net.mindview.util.Generator;

import java.util.List;
import java.util.concurrent.*;

/**
 * Exercise 34: (1) Modify ExchangerDemo.java to use your own class instead of Fat.
 Concurrency
 */
public class Exercise34 {
    static class ExchangerProducer<T> implements Runnable {
        private Generator<T> generator;
        private Exchanger<List<T>> exchanger;
        private List<T> holder;
        ExchangerProducer(Exchanger<List<T>> exchg,
                          Generator<T> gen, List<T> holder) {
            exchanger = exchg;
            generator = gen;
            this.holder = holder;
        }
        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    for(int i = 0; i < ExchangerDemo.size; i++)
                        holder.add(generator.next());
                    // Exchange full for empty:
                    holder = exchanger.exchange(holder);
                }
            } catch(InterruptedException e) {
                // OK to terminate this way.
            }
        }
    }

    static class ExchangerConsumer<T> implements Runnable {
        private Exchanger<List<T>> exchanger;
        private List<T> holder;
        private volatile T value;
        ExchangerConsumer(Exchanger<List<T>> ex, List<T> holder){
            exchanger = ex;
            this.holder = holder;
        }
        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    holder = exchanger.exchange(holder);
                    for(T x : holder) {
                        value = x; // Fetch out value
                        holder.remove(x); // OK for CopyOnWriteArrayList
                    }
                }
            } catch(InterruptedException e) {
                // OK to terminate this way.
            }
            System.out.println("Final value: " + value);
        }
    }

    static class ExchangerDemo<T> {
        static int size = 10;
        static int delay = 5; // Seconds

        public static void main(String[] args) throws Exception {
            if(args.length > 0)
                size = new Integer(args[0]);
            if(args.length > 1)
                delay = new Integer(args[1]);
            ExecutorService exec = Executors.newCachedThreadPool();
            Exchanger<List<Exercise34>> xc = new Exchanger<List<Exercise34>>();
            List<Exercise34>
                    producerList = new CopyOnWriteArrayList<Exercise34>(),
                    consumerList = new CopyOnWriteArrayList<Exercise34>();
            exec.execute(new ExchangerProducer<Exercise34>(xc,
                    BasicGenerator.create(Exercise34.class), producerList));
            exec.execute(
                    new ExchangerConsumer<Exercise34>(xc,consumerList));
            TimeUnit.SECONDS.sleep(delay);
            exec.shutdownNow();
        }
    }

    private static int counter = 0;
    private final int id = counter++;

    public void operation() { System.out.println(this); }
    public String toString() { return "Exercise34 id: " + id; }

    public static void main(String[] args) throws Exception {
        ExchangerDemo.main(args);
    }
}
