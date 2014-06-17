package concurrency.exercises;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static net.mindview.util.Print.print;
import static net.mindview.util.Print.printnb;

public class Exercise26 {

    static class Meal {
        private final int orderNum;

        public Meal(int orderNum) {
            this.orderNum = orderNum;
        }

        public String toString() {
            return "Meal " + orderNum;
        }
    }

    static class WaitPerson implements Runnable {
        private Restaurant restaurant;

        public WaitPerson(Restaurant r) {
            restaurant = r;
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    synchronized (this) {
                        while (restaurant.meal == null)
                            wait(); // ... for the chef to produce a meal
                    }
                    print("Waitperson got " + restaurant.meal);

                    synchronized (restaurant.busBoy) {
                        restaurant.busBoy.notifyAll(); // tell busboy to clean meal
                    }

                    synchronized (this) {
                        while (restaurant.meal != null)
                            wait();// ... for the busboy to clean a meal
                    }

                    synchronized (restaurant.chef) {
                        restaurant.chef.notifyAll(); // order new meal from chef, because people want more
                    }
                }
            } catch (InterruptedException e) {
                print("WaitPerson interrupted");
            }
        }
    }

    static class Chef implements Runnable {
        private Restaurant restaurant;
        private int count = 0;

        public Chef(Restaurant r) {
            restaurant = r;
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    synchronized (this) {
                        while (restaurant.meal != null)
                            wait(); // ... for the meal to be taken
                    }
                    if (++count == 10) {
                        print("Out of food, closing");
                        restaurant.exec.shutdownNow();
                        return;
                    }
                    printnb("Order up! ");
                    synchronized (restaurant.waitPerson) {
                        restaurant.meal = new Meal(count);
                        print("Chef produced new meal: " + restaurant.meal);
                        restaurant.waitPerson.notifyAll();
                    }
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            } catch (InterruptedException e) {
                print("Chef interrupted");
            }
        }
    }

    static class BusBoy implements Runnable {
        private Restaurant restaurant;

        BusBoy(Restaurant restaurant) {
            this.restaurant = restaurant;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    synchronized (this) {
                        while (restaurant.meal == null)
                            wait(); // ... for the chef to produce a meal
                    }
                    print("BusBoy is cleaning " + restaurant.meal);

                    synchronized (restaurant.waitPerson) {
                        restaurant.meal = null;
                        restaurant.waitPerson.notifyAll(); // Let waitperson know that it is all clean now
                    }
                }
            } catch (InterruptedException e) {
                print("BusBoy interrupted");
            }
        }
    }

    static class Restaurant {
        Meal meal;
        ExecutorService exec = Executors.newCachedThreadPool();
        WaitPerson waitPerson = new WaitPerson(this);
        Chef chef = new Chef(this);
        BusBoy busBoy = new BusBoy(this);

        public Restaurant() {
            exec.execute(chef);
            exec.execute(waitPerson);
            exec.execute(busBoy);
        }

        public static void main(String[] args) {
            new Restaurant();
        }
    }

    public static void main(String[] args) {
        Restaurant.main(args);
    }
}
