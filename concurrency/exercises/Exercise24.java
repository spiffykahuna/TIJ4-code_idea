package concurrency.exercises;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Item {
    private final int itemID;
    public Item(int itemID) { this.itemID = itemID; }
    public String toString() { return "Item " + itemID; }
}

class Producer implements Runnable {

    private Random rand = new Random();

    private final Queue<Item> queue;

    private static int idCounter;

    Producer(Queue<Item> queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                Item item = new Item(++idCounter);
                synchronized (queue) {
                    while(!queue.offer(item)) {
                        queue.wait();
                    }
                    System.out.println("Produced item: " + item);
                    queue.notifyAll();
                }
                if(rand.nextBoolean())
                    TimeUnit.MILLISECONDS.sleep(rand.nextInt(1000));
            }
        } catch (InterruptedException ignored) {}
    }
}

class Consumer implements Runnable {

    private Random rand = new Random();

    private final Queue<Item> queue;

    private static int idCounter;

    Consumer(Queue<Item> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                synchronized (queue) {
                    Item item = null;
                    while((item = queue.poll()) == null) {
                        queue.wait();
                    }
                    System.out.println("Consumed item: " + item + " . Items remaining: " + queue.size());
                    queue.notifyAll();
                }
                if(rand.nextBoolean())
                    TimeUnit.MILLISECONDS.sleep(rand.nextInt(1000));
            }
        } catch (InterruptedException ignored) {}
    }
}

public class Exercise24 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Queue<Item> queue = new ArrayBlockingQueue<Item>(10);
        exec.execute(new Producer(queue));
        exec.execute(new Consumer(queue));

        Thread.sleep(60000);
        exec.shutdownNow();
    }
}
