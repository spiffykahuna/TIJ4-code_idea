package concurrency.exercises;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static net.mindview.util.Print.print;
import static net.mindview.util.Print.printnb;

public class Exercise27 {
    static class Meal {
        private final int orderNum;
        public Meal(int orderNum) { this.orderNum = orderNum; }
        public String toString() { return "Meal " + orderNum; }
    }

    static class WaitPerson implements Runnable {
        private Restaurant restaurant;
        public WaitPerson(Restaurant r) { restaurant = r; }
        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    waitForMeal();
                    print("Waitperson got " + restaurant.meal);
                    notifyChef();
                }
            } catch(InterruptedException e) {
                print("WaitPerson interrupted");
            }
        }

        private void notifyChef() {
            restaurant.lock.lock();
            try {
                restaurant.meal = null;
                restaurant.condition.signalAll();
            } finally {
                restaurant.lock.unlock();
            }
        }

        private void waitForMeal() throws InterruptedException {
            restaurant.lock.lock();
            try {
                while(restaurant.meal == null)
                    restaurant.condition.await();
            } finally {
                restaurant.lock.unlock();
            }
        }
    }

    static class Chef implements Runnable {
        private Restaurant restaurant;
        private int count = 0;
        public Chef(Restaurant r) { restaurant = r; }
        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    waitForMealToBeTaken();
                    if(++count == 10) {
                        print("Out of food, closing");
                        restaurant.exec.shutdownNow();
                        return;
                    }
                    printnb("Order up! ");
                    prepareMeal();
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            } catch(InterruptedException e) {
                print("Chef interrupted");
            }
        }

        private void prepareMeal() {
            restaurant.lock.lock();
            try {
                restaurant.meal = new Meal(count);
                restaurant.condition.signalAll();
            } finally {
                restaurant.lock.unlock();
            }
        }

        private void waitForMealToBeTaken() throws InterruptedException {
            restaurant.lock.lock();
            try {
                while(restaurant.meal != null)
                    restaurant.condition.await();
            } finally {
                restaurant.lock.unlock();
            }
        }
    }

    static class Restaurant {
        Meal meal;
        ExecutorService exec = Executors.newCachedThreadPool();
        WaitPerson waitPerson = new WaitPerson(this);
        Chef chef = new Chef(this);

        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        public Restaurant() {
            exec.execute(chef);
            exec.execute(waitPerson);
        }
        public static void main(String[] args) {
            new Restaurant();
        }
    }

    public static void main(String[] args) {
        Restaurant.main(args);
    }
}
