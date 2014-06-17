package concurrency.exercises;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Exercise17 {

    static class RadiationCounter {
        interface RadiationSensor extends Runnable {
            int getCountRate();
        }

        static volatile boolean canceled = false;

        List<RadiationSensor> sensors = new ArrayList<RadiationSensor>();
        ExecutorService exec = Executors.newCachedThreadPool();

        void addRadiationSensor(RadiationSensor sensor) { sensors.add(sensor); }

        int measureFor(int timeAmount, TimeUnit timeUnit) throws InterruptedException {
            canceled = false;
            System.out.println("Starting sensors...");
            for(RadiationSensor sensor: sensors) {
                exec.execute(sensor);
            }

            System.out.println("Measuring...");
            timeUnit.sleep(timeAmount);
            System.out.println("Stopping sensors...");
            canceled = true;
            exec.shutdown();
            if(!exec.awaitTermination(2000, TimeUnit.MILLISECONDS))
                System.out.println("Some sensors were not terminated!");

            int totalCountRate = 0;
            for(RadiationSensor sensor: sensors) {
                totalCountRate += sensor.getCountRate();
            }
            return totalCountRate;
        }

        private static abstract class AbstractSensor implements RadiationSensor {
            private int rate;
            private Random rand = new Random(47);

            @Override
            public int getCountRate() {
                return rate;
            }

            @Override
            public void run() {
                while(!canceled) {
                    rate += rand.nextInt(1000) * getMultiplier();
                    try {
                        Thread.sleep(rand.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            abstract int getMultiplier();
        }

        static class AlphaSensor extends AbstractSensor {
            @Override
            int getMultiplier() {
                return 1;
            }
        }

        static class BetaSensor extends AbstractSensor {
            @Override
            int getMultiplier() {
                return 4;
            }
        }

        static class GammaSensor extends AbstractSensor {
            @Override
            int getMultiplier() {
                return 8;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        RadiationCounter radiationCounter = new RadiationCounter();
        for (int i = 0; i < 5; i++) {
            radiationCounter.addRadiationSensor(new RadiationCounter.AlphaSensor());
            radiationCounter.addRadiationSensor(new RadiationCounter.BetaSensor());
            radiationCounter.addRadiationSensor(new RadiationCounter.GammaSensor());
        }
        System.out.println("Total amout within 5 seconds is: " + radiationCounter.measureFor(5, TimeUnit.SECONDS));
    }
}
