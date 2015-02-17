package concurrency.exercises;

import java.util.Random;
import java.util.concurrent.*;

import static net.mindview.util.Print.print;
import static net.mindview.util.Print.printnb;

/**
 *  Exercise 42: (7) Modify WaxOMatic.java so that it implements active objects.
 *
 */
 public class Exercise42 {
    private abstract static class ActiveObject {
        protected ExecutorService ex =
                Executors.newCachedThreadPool();
        private Random rand = new Random(47);

        private final String activeObjectName;

        private static long counter;
        private final long id = counter++;

        protected ActiveObject() {
            this.activeObjectName = getClass().getSimpleName();
        }

        // Insert a random delay to produce the effect
        // of a calculation time:
        protected void pause(int factor) {
            try {
                TimeUnit.MILLISECONDS.sleep(
                        100 + rand.nextInt(factor));
            } catch(InterruptedException e) {
                print("sleep() interrupted");
            }
        }


        public void shutdown() { ex.shutdown(); }

        @Override
        public String toString() {
            return activeObjectName + "{" +
                    "id=" + id +
                    '}';
        }
    }

    private static class Car extends ActiveObject {
        private volatile boolean waxOn = false;

        public void waxed(final WaxOn waxOnActiveObject) {
            ex.submit(new Runnable() {
                @Override
                public void run() {
                    pause(100);
                    print(getCar() + " was waxed by " + waxOnActiveObject);
                    waxOn = true;
                }
            });
        }

        public void buffed(final WaxOff waxOffActiveObject) {
            ex.submit(new Runnable() {
                @Override
                public void run() {
                    pause(100);
                    print(getCar() + " was buffed by " + waxOffActiveObject);
                    waxOn = false;
                }
            });
        }

        public void notifyWhenWaxIsOff(final WaxOn waxOnActiveObject) {

            ex.submit(new Runnable() {
                @Override
                public void run() {
                    do {
                        pause(200);
                        if(ex.isShutdown()) return;
                    } while(waxOn);
                    waxOnActiveObject.onWaxOff(getCar());
                }
            });
        }

        public void notifyWhenWaxIsOn(final WaxOff waxOffActiveObject) {

            ex.submit(new Runnable() {
                @Override
                public void run() {
                    do {
                        pause(200);
                        if(ex.isShutdown()) return;
                    } while(!waxOn);
                    waxOffActiveObject.onWaxOn(getCar());
                }
            });
        }



        private synchronized Car getCar() {
            return this;
        }
    }

    static class WaxOn extends ActiveObject {
        public void onWaxOff(final Car car) {
            final WaxOn waxOn = this;
            ex.submit(new Runnable() {
                @Override
                public void run() {
                    print(waxOn + " is waxing a " + car);
                    car.waxed(waxOn);

                    pause(100);

                    print(waxOn + " is waiting for buffing");
                    car.notifyWhenWaxIsOff(waxOn);
                }
            });
        }
    }

    static class WaxOff extends ActiveObject {
        public void onWaxOn(final Car car) {
            final WaxOff waxOff = this;
            ex.submit(new Runnable() {
                @Override
                public void run() {
                    print(waxOff + " is buffing a " + car);
                    car.buffed(waxOff);

                    pause(100);

                    print(waxOff + " is waiting for waxing");
                    car.notifyWhenWaxIsOn(waxOff);
                }
            });
        }
    }

    public static class WaxOMatic {
        public static void main(String[] args) throws Exception {
            Car car = new Car();
            WaxOn waxOn = new WaxOn();
            WaxOff waxOff = new WaxOff();

            car.notifyWhenWaxIsOff(waxOn);
            car.notifyWhenWaxIsOn(waxOff);


            TimeUnit.SECONDS.sleep(5); // Run for a while...
            print("GLOBAL SHUTDOWN");
            waxOn.shutdown();
            waxOff.shutdown();

            car.shutdown();
        }
    } /* Output: (95% match)
Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! Wax On! Wax Off! Wax On! Exiting via interrupt
Ending Wax On task
Exiting via interrupt
Ending Wax Off task
*///:~

    public static void main(String[] args) throws Exception {
        WaxOMatic.main(args);
    }

}
