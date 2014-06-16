package concurrency.exercises;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class Exercise14 {
    public static final int COUNT = 1000;
    public static void main(String[] args) throws InterruptedException {
        final AtomicInteger totalExecuted = new AtomicInteger(0);

        List<Timer> timers = new ArrayList<Timer>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    totalExecuted.incrementAndGet();
                }
            }, 5000);
            timers.add(t);
        }

        TimeUnit.SECONDS.sleep(6);

        System.out.println("Total executed: " + totalExecuted.get() + " of " + COUNT );

        for(Timer t: timers) {
            t.cancel();
        }
    }
}
