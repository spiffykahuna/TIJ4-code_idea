package concurrency.exercises;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

import static net.mindview.util.Print.print;

public class Exercise37 {
    static class Car {
        private final int id;
        private boolean
                engine = false, driveTrain = false, wheels = false;

        private boolean
                exhaust = false, body = false, fenders = false;

        public Car(int idn)  { id = idn; }
        // Empty Car object:
        public Car()  { id = -1; }
        public synchronized int getId() { return id; }
        public synchronized void addEngine() { engine = true; }
        public synchronized void addDriveTrain() {
            driveTrain = true;
        }
        public synchronized void addWheels() { wheels = true; }

        public synchronized void addExhaust() { exhaust = true; }
        public synchronized void addBody() { body = true; }
        public synchronized void addFenders() { fenders = true; }

        public synchronized String toString() {
            return "Car " + id + " [" +
                    " engine: " + engine
                    + " driveTrain: " + driveTrain
                    + " wheels: " + wheels
                    + " exhaust: " + exhaust
                    + " body: " + body
                    + " fenders: " + fenders
                    + " ]";
        }
    }

    static class CarQueue extends LinkedBlockingQueue<Car> {}

    static class ChassisBuilder implements Runnable {
        private CarQueue carQueue;
        private int counter = 0;
        public ChassisBuilder(CarQueue cq) { carQueue = cq; }
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    // Make chassis:
                    Car c = new Car(counter++);
                    print("ChassisBuilder created " + c);
                    // Insert into queue
                    carQueue.put(c);
                }
            } catch(InterruptedException e) {
                print("Interrupted: ChassisBuilder");
            }
            print("ChassisBuilder off");
        }
    }

    static interface Assembler extends Runnable {
        public Car car();
        public void actionDone(Robot robot);
    }

    static abstract class AbstractAssembler implements Assembler {
        protected CarQueue incomingQueue, finishingQueue;
        protected Car car;
        protected CyclicBarrier barrier;
        protected RobotPool robotPool;

        public AbstractAssembler(CarQueue cq, CarQueue fq, RobotPool rp, int robotCount){
            incomingQueue = cq;
            finishingQueue = fq;
            robotPool = rp;
            barrier =  new CyclicBarrier(robotCount + 1 /* this.barrier.await() */);
        }

        public Car car() { return car; }

        public void actionDone(Robot robot) {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                print("Exiting Assembler via interrupt");
            } catch (BrokenBarrierException e) {
                // This one we want to know about
                throw new RuntimeException(e);
            }
        }

        public void run() {
            try {
                while(!Thread.interrupted()) {
                    // Blocks until chassis is available:
                    car = incomingQueue.take();
                    // Hire robots to perform work:
                    assemble();
                    barrier.await(); // Until the robots finish
                    // Put car into finishingQueue for further work
                    finishingQueue.put(car);
                }
            } catch(InterruptedException e) {
                print("Exiting Assembler via interrupt");
            } catch(BrokenBarrierException e) {
                // This one we want to know about
                throw new RuntimeException(e);
            }
            print("Assembler off");
        }

        protected abstract void assemble() throws InterruptedException;
    }

    static abstract class ParallelAssembler extends AbstractAssembler {
        private CountDownLatch actionLatch;

        public ParallelAssembler(CarQueue cq, CarQueue fq, RobotPool rp, int robotCount) {
            super(cq, fq, rp, robotCount);
            actionLatch = new CountDownLatch(robotCount);
        }

        @Override
        public void actionDone(Robot robot) {
            actionLatch.countDown();
        }

        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    // Blocks until chassis is available:
                    car = incomingQueue.take();
                    // Hire robots to perform work:
                    assemble();
                    actionLatch.await(); // Until the robots finish
                    // Put car into finishingQueue for further work
                    finishingQueue.put(car);
                }
            } catch(InterruptedException e) {
                print("Exiting Assembler via interrupt");
            }
            print("Assembler off");
        }
    }



    static class FirstAssembler extends AbstractAssembler {
        public FirstAssembler(CarQueue cq, CarQueue fq, RobotPool rp, int robotCount) {
            super(cq, fq, rp, robotCount);
        }

        @Override
        protected void assemble() throws InterruptedException {
            robotPool.hire(EngineRobot.class, this);
            robotPool.hire(DriveTrainRobot.class, this);
            robotPool.hire(WheelRobot.class, this);
        }
    }

    static class SecondAssembler extends ParallelAssembler {

        public SecondAssembler(CarQueue cq, CarQueue fq, RobotPool rp, int robotCount) {
            super(cq, fq, rp, robotCount);
        }

        @Override
        protected void assemble() throws InterruptedException {
            robotPool.hire(ExhaustRobot.class, this);
            robotPool.hire(BodyRobot.class, this);
            robotPool.hire(FendersRobot.class, this);
        }
    }

    static class Reporter implements Runnable {
        private CarQueue carQueue;
        public Reporter(CarQueue cq) { carQueue = cq; }
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    print("New car arrived -->" + carQueue.take());
                }
            } catch(InterruptedException e) {
                print("Exiting Reporter via interrupt");
            }
            print("Reporter off");
        }
    }

    static abstract class Robot implements Runnable {
        private RobotPool pool;
        public Robot(RobotPool p) { pool = p; }
        protected Assembler assembler;
        public Robot assignAssembler(Assembler assembler) {
            this.assembler = assembler;
            return this;
        }
        private boolean engage = false;
        public synchronized void engage() {
            engage = true;
            notifyAll();
        }
        // The part of run() that's different for each robot:
        abstract protected void performService();
        public void run() {
            try {
                powerDown(); // Wait until needed
                while(!Thread.interrupted()) {
                    performService();
                    assembler.actionDone(this); // Synchronize
                    // We're done with that job...
                    powerDown();
                }
            } catch(InterruptedException e) {
                print("Exiting " + this + " via interrupt");
            }
            print(this + " off");
        }
        private synchronized void
        powerDown() throws InterruptedException {
            engage = false;
            assembler = null; // Disconnect from the Assembler
            // Put ourselves back in the available pool:
            pool.release(this);
            while(engage == false)  // Power down
                wait();
        }
        public String toString() { return getClass().getName(); }
    }

    static class EngineRobot extends Robot {
        public EngineRobot(RobotPool pool) { super(pool); }
        protected void performService() {
            print(this + " installing Engine");
            assembler.car().addEngine();
        }
    }

    static class DriveTrainRobot extends Robot {
        public DriveTrainRobot(RobotPool pool) { super(pool); }
        protected void performService() {
            print(this + " installing DriveTrain");
            assembler.car().addDriveTrain();
        }
    }

    static class WheelRobot extends Robot {
        public WheelRobot(RobotPool pool) { super(pool); }
        protected void performService() {
            print(this + " installing Wheels");
            assembler.car().addWheels();
        }
    }

    static class ExhaustRobot extends Robot {
        public ExhaustRobot(RobotPool pool) { super(pool); }
        protected void performService() {
            print(this + " installing Exhaust");
            assembler.car().addExhaust();
        }
    }

    static class BodyRobot extends Robot {
        public BodyRobot(RobotPool pool) { super(pool); }
        protected void performService() {
            print(this + " installing Body");
            assembler.car().addBody();
        }
    }

    static class FendersRobot extends Robot {
        public FendersRobot(RobotPool pool) { super(pool); }
        protected void performService() {
            print(this + " installing Fenders");
            assembler.car().addFenders();
        }
    }

    static class RobotPool {
        // Quietly prevents identical entries:
        private Set<Robot> pool = new HashSet<Robot>();
        public synchronized void add(Robot r) {
            pool.add(r);
            notifyAll();
        }
        public synchronized void
        hire(Class<? extends Robot> robotType, Assembler d)
                throws InterruptedException {
            for(Robot r : pool)
                if(r.getClass().equals(robotType)) {
                    pool.remove(r);
                    r.assignAssembler(d);
                    r.engage(); // Power it up to do the task
                    return;
                }
            wait(); // None available
            hire(robotType, d); // Try again, recursively
        }
        public synchronized void release(Robot r) { add(r); }
    }

    static class CarBuilder {
        public static void main(String[] args) throws Exception {
            CarQueue chassisQueue = new CarQueue(),
                    middleQueue = new CarQueue(),
                    finishingQueue = new CarQueue();
            ExecutorService exec = Executors.newCachedThreadPool();
            RobotPool robotPool = new RobotPool();

            exec.execute(new EngineRobot(robotPool));
            exec.execute(new DriveTrainRobot(robotPool));
            exec.execute(new WheelRobot(robotPool));

            exec.execute(new FirstAssembler(chassisQueue, middleQueue, robotPool, 3));

            exec.execute(new ExhaustRobot(robotPool));
            exec.execute(new BodyRobot(robotPool));
            exec.execute(new FendersRobot(robotPool));

            exec.execute(new SecondAssembler(middleQueue, finishingQueue, robotPool, 3));

            exec.execute(new Reporter(finishingQueue));
            // Start everything running by producing chassis:
            exec.execute(new ChassisBuilder(chassisQueue));
            TimeUnit.SECONDS.sleep(7);
            exec.shutdownNow();
        }
    }

    public static void main(String[] args) throws Exception {
        CarBuilder.main(args);
    }
}
