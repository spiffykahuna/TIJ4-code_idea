package concurrency.exercises;

import java.util.*;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static net.mindview.util.Print.print;
import static net.mindview.util.Print.printnb;

/*
Exercise 33: (7) Modify GreenhouseScheduler.java so that it uses a DelayQueue instead of a ScheduledExecutor.
 */
public class Exercise33 {
    static class DelayedTask implements Runnable, Delayed {
        private static int counter = 0;
        private final int id = counter++;
        private final long delta;
        private final long trigger;
        protected static List<DelayedTask> sequence =
                new ArrayList<DelayedTask>();

        protected final Runnable action;

        public DelayedTask(Runnable action, long delayInMilliseconds) {
            this.action = action;
            delta = delayInMilliseconds;
            trigger = System.nanoTime() +
                    NANOSECONDS.convert(delta, MILLISECONDS);
            sequence.add(this);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(
                    trigger - System.nanoTime(), NANOSECONDS);
        }
        @Override
        public int compareTo(Delayed arg) {
            DelayedTask that = (DelayedTask)arg;
            if(trigger < that.trigger) return -1;
            if(trigger > that.trigger) return 1;
            return 0;
        }

        @Override
        public void run() {
            action.run();
        }


        public String summary() {
            return "(" + id + ":" + delta + ")";
        }

        public static class EndSentinel extends DelayedTask {
            private ExecutorService exec;
            public EndSentinel(int delay, ExecutorService e) {
                super(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, delay);
                exec = e;
            }
            @Override
            public void run() {
                for(DelayedTask pt : sequence) {
                    printnb(pt.summary() + " ");
                }
                print();
                print(this + " Calling shutdownNow()");
                exec.shutdownNow();
            }
        }
    }

    static class FixedRateTask extends DelayedTask {

        private final long period;
        private final DelayQueue<DelayedTask> taskQueue;

        public FixedRateTask(Runnable action, long initialDelay, long period, DelayQueue<DelayedTask> taskQueue) {
            super(action, initialDelay);
            this.period = period;
            this.taskQueue = taskQueue;
        }

        @Override
        public void run() {
            taskQueue.add(
                new FixedRateTask(action, period, period, taskQueue)
            );
            super.run();
        }
    }

    static class DelayedTaskConsumer implements Runnable {
        private DelayQueue<DelayedTask> q;
        public DelayedTaskConsumer(DelayQueue<DelayedTask> q) {
            this.q = q;
        }

        @Override
        public void run() {
            try {
                while(!Thread.interrupted())
                    q.take().run(); // Run task with the current thread
            } catch(InterruptedException e) {
                // Acceptable way to exit
            }
            print("Finished DelayedTaskConsumer");
        }
    }

    static class GreenhouseScheduler {
        private volatile boolean light = false;
        private volatile boolean water = false;
        private String thermostat = "Day";

        public GreenhouseScheduler() {
            exec.execute(new DelayedTaskConsumer(delayQueue));
        }

        public synchronized String getThermostat() {
            return thermostat;
        }
        public synchronized void setThermostat(String value) {
            thermostat = value;
        }

        ExecutorService exec = Executors.newCachedThreadPool();
        DelayQueue<DelayedTask> delayQueue = new DelayQueue<DelayedTask>();

        public void schedule(Runnable event, long delay) {
            delayQueue.add(new DelayedTask(event, delay));
        }

        public void repeat(Runnable event, long initialDelay, long period) {
            delayQueue.add(
                new FixedRateTask(event, initialDelay, period, delayQueue)
            );
        }
        class LightOn implements Runnable {
            @Override
            public void run() {
                // Put hardware control code here to
                // physically turn on the light.
                System.out.println("Turning on lights");
                light = true;
            }
        }
        class LightOff implements Runnable {
            @Override
            public void run() {
                // Put hardware control code here to
                // physically turn off the light.
                System.out.println("Turning off lights");
                light = false;
            }
        }
        class WaterOn implements Runnable {
            @Override
            public void run() {
                // Put hardware control code here.
                System.out.println("Turning greenhouse water on");
                water = true;
            }
        }
        class WaterOff implements Runnable {
            @Override
            public void run() {
                // Put hardware control code here.
                System.out.println("Turning greenhouse water off");
                water = false;
            }
        }
        class ThermostatNight implements Runnable {
            @Override
            public void run() {
                // Put hardware control code here.
                System.out.println("Thermostat to night setting");
                setThermostat("Night");
            }
        }
        class ThermostatDay implements Runnable {
            @Override
            public void run() {
                // Put hardware control code here.
                System.out.println("Thermostat to day setting");
                setThermostat("Day");
            }
        }
        class Bell implements Runnable {
            @Override
            public void run() { System.out.println("Bing!"); }
        }
        class Terminate implements Runnable {
            @Override
            public void run() {
                System.out.println("Terminating");
                exec.shutdownNow();
                // Must start a separate task to do this job,
                // since the scheduler has been shut down:
                new Thread() {
                    @Override
                    public void run() {
                        for(DataPoint d : data)
                            System.out.println(d);
                    }
                }.start();
            }
        }
        // New feature: data collection
        static class DataPoint {
            final Calendar time;
            final float temperature;
            final float humidity;
            public DataPoint(Calendar d, float temp, float hum) {
                time = d;
                temperature = temp;
                humidity = hum;
            }
            public String toString() {
                return time.getTime() +
                        String.format(
                                " temperature: %1$.1f humidity: %2$.2f",
                                temperature, humidity);
            }
        }
        private Calendar lastTime = Calendar.getInstance();
        { // Adjust date to the half hour
            lastTime.set(Calendar.MINUTE, 30);
            lastTime.set(Calendar.SECOND, 0);
        }
        private float lastTemp = 65.0f;
        private int tempDirection = +1;
        private float lastHumidity = 50.0f;
        private int humidityDirection = +1;
        private Random rand = new Random(47);
        List<DataPoint> data = Collections.synchronizedList(
                new ArrayList<DataPoint>());
        class CollectData implements Runnable {
            @Override
            public void run() {
                System.out.println("Collecting data");
                synchronized(GreenhouseScheduler.this) {
                    // Pretend the interval is longer than it is:
                    lastTime.set(Calendar.MINUTE,
                            lastTime.get(Calendar.MINUTE) + 30);
                    // One in 5 chances of reversing the direction:
                    if(rand.nextInt(5) == 4)
                        tempDirection = -tempDirection;
                    // Store previous value:
                    lastTemp = lastTemp +
                            tempDirection * (1.0f + rand.nextFloat());
                    if(rand.nextInt(5) == 4)
                        humidityDirection = -humidityDirection;
                    lastHumidity = lastHumidity +
                            humidityDirection * rand.nextFloat();
                    // Calendar must be cloned, otherwise all
                    // DataPoints hold references to the same lastTime.
                    // For a basic object like Calendar, clone() is OK.
                    data.add(new DataPoint((Calendar)lastTime.clone(),
                            lastTemp, lastHumidity));
                }
            }
        }

        public static void main(String[] args) {
            GreenhouseScheduler gh = new GreenhouseScheduler();
            gh.schedule(gh.new Terminate(), 5000);
            // Former "Restart" class not necessary:
            gh.repeat(gh.new Bell(), 0, 1000);
            gh.repeat(gh.new ThermostatNight(), 0, 2000);
            gh.repeat(gh.new LightOn(), 0, 200);
            gh.repeat(gh.new LightOff(), 0, 400);
            gh.repeat(gh.new WaterOn(), 0, 600);
            gh.repeat(gh.new WaterOff(), 0, 800);
            gh.repeat(gh.new ThermostatDay(), 0, 1400);
            gh.repeat(gh.new CollectData(), 500, 500);
        }
    }

    public static void main(String[] args) {
        GreenhouseScheduler.main(args);
    }
}
