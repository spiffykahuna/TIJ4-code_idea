package concurrency.exercises;

import concurrency.LiftOff;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Exercise20 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool(2); // <-- limit thread count to see results
        for(int i = 0; i < 5; i++)
            exec.execute(new LiftOff());
        exec.shutdownNow();
        if(!exec.awaitTermination(250, TimeUnit.MILLISECONDS))
            System.out.println("Some tasks were not terminated");
    }
}
