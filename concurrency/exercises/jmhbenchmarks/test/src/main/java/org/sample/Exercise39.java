package org.sample;



import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//@State(Scope.Group)

public class Exercise39 {


    static final int N_ELEMENTS = 100000;
    static final int N_GENES = 30;
    static final int N_EVOLVERS = 50;

    @State(Scope.Benchmark)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public static class AtomicIntegerArray {
        private AtomicInteger[][] GRID_OBJECTS;
        private Random rand = new Random(47);

        @Setup
        public void up() {
            GRID_OBJECTS = new AtomicInteger[N_ELEMENTS][N_GENES];

            for(int i = 0; i < N_ELEMENTS; i++)
                for(int j = 0; j < N_GENES; j++) {
                    GRID_OBJECTS[i][j] = new AtomicInteger(rand.nextInt(1000));
                }
        }

        @Benchmark
        public void calc() {
            // Randomly select an element to work on:
            int element = rand.nextInt(N_ELEMENTS);
            for(int i = 0; i < N_GENES; i++) {
                int previous = element - 1;
                if(previous < 0) previous = N_ELEMENTS - 1;
                int next = element + 1;
                if(next >= N_ELEMENTS) next = 0;
                int oldvalue = GRID_OBJECTS[element][i].get();
                // Perform some kind of modeling calculation:
                int newvalue = oldvalue +
                        GRID_OBJECTS[previous][i].get() + GRID_OBJECTS[next][i].get();
                newvalue /= 3; // Average the three values
                if(!GRID_OBJECTS[element][i]
                        .compareAndSet(oldvalue, newvalue)) {
                    // Policy here to deal with failure. Here, we
                    // just report it and ignore it; our model
                    // will eventually deal with it.
                    System.out.println("Old value changed from " + oldvalue);
                }
            }
        }

    }

    @State(Scope.Benchmark)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public static class SimpleIntegerArrayWithGlobalLock {
        private int[][] GRID_ARRAY;
        private Random rand = new Random(47);

        private Lock lock = new ReentrantLock();

        @Setup
        public void up() {
            GRID_ARRAY = new int[N_ELEMENTS][N_GENES];
            for(int i = 0; i < N_ELEMENTS; i++)
                for(int j = 0; j < N_GENES; j++) {
                    GRID_ARRAY[i][j] = rand.nextInt(1000);
                }
        }

        @Benchmark
        public void calc() {
            // Randomly select an element to work on:
            int element = rand.nextInt(N_ELEMENTS);
            lock.lock();
            try{
                for(int i = 0; i < N_GENES; i++) {
                    int previous = element - 1;
                    if(previous < 0) previous = N_ELEMENTS - 1;
                    int next = element + 1;
                    if(next >= N_ELEMENTS) next = 0;

                        int oldvalue = GRID_ARRAY[element][i];
                        // Perform some kind of modeling calculation:
                        int newvalue = oldvalue +
                                GRID_ARRAY[previous][i] + GRID_ARRAY[next][i];
                        newvalue /= 3; // Average the three values

                        if(GRID_ARRAY[element][i] == oldvalue) {
                            GRID_ARRAY[element][i] = newvalue;
                        } else {
                            // Policy here to deal with failure. Here, we
                            // just report it and ignore it; our model
                            // will eventually deal with it.
                            System.out.println("Old value changed from " + oldvalue);
                        }
                }
            } finally {
                lock.unlock();
            }
        }
    }

    @State(Scope.Benchmark)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public static class SimpleIntegerArrayWithElementLevelLocks {
        private int[][] GRID_ARRAY;
        private Lock[] elementLocks;
        private Random rand = new Random(47);

        private Lock lock = new ReentrantLock();

        @Setup
        public void up() {
            GRID_ARRAY = new int[N_ELEMENTS][N_GENES];

            elementLocks = new ReentrantLock[N_ELEMENTS];

            for (int i = 0; i < N_ELEMENTS; i++) {
                for (int j = 0; j < N_GENES; j++) {
                    GRID_ARRAY[i][j] = rand.nextInt(1000);
                }
                elementLocks[i] = new ReentrantLock();
            }
        }

        @Benchmark
        public void calc() {
            // Randomly select an element to work on:
            int element = rand.nextInt(N_ELEMENTS);

            int previous = element - 1;
            if (previous < 0) previous = N_ELEMENTS - 1;
            int next = element + 1;
            if (next >= N_ELEMENTS) next = 0;


            for (int i = 0; i < N_GENES; i++) {

                int oldvalue = 0;
                elementLocks[element].lock();
                try {
                    oldvalue = GRID_ARRAY[element][i];
                } finally {
                    elementLocks[element].unlock();
                }


                int previousValue = 0;
                elementLocks[previous].lock();
                try {
                    previousValue = GRID_ARRAY[previous][i];
                } finally {
                    elementLocks[previous].unlock();
                }

                int nextValue = 0;
                elementLocks[next].lock();
                try {
                    nextValue = GRID_ARRAY[next][i];
                } finally {
                    elementLocks[next].unlock();
                }


                // Perform some kind of modeling calculation:
                int newvalue = oldvalue + previousValue + nextValue;
                newvalue /= 3; // Average the three values


                elementLocks[element].lock();
                try {
                    if (GRID_ARRAY[element][i] == oldvalue) {
                        GRID_ARRAY[element][i] = newvalue;
                    } else {
                        // Policy here to deal with failure. Here, we
                        // just report it and ignore it; our model
                        // will eventually deal with it.
                        System.out.println("Old value changed from " + oldvalue);
                    }
                } finally {
                    elementLocks[element].unlock();
                }


            }

        }
    }

    @State(Scope.Benchmark)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public static class SimpleIntegerArrayWithGeneLevelLocks {
        private int[][] GRID_ARRAY;
        private Lock[][] elementLocks;
        private Random rand = new Random(47);

        private Lock lock = new ReentrantLock();

        @Setup
        public void up() {
            GRID_ARRAY = new int[N_ELEMENTS][N_GENES];

            elementLocks = new ReentrantLock[N_ELEMENTS][N_GENES];

            for (int i = 0; i < N_ELEMENTS; i++) {
                for (int j = 0; j < N_GENES; j++) {
                    GRID_ARRAY[i][j] = rand.nextInt(1000);
                    elementLocks[i][j] = new ReentrantLock();
                }

            }
        }

        @Benchmark
        public void calc() {
            // Randomly select an element to work on:
            int element = rand.nextInt(N_ELEMENTS);

            int previous = element - 1;
            if (previous < 0) previous = N_ELEMENTS - 1;
            int next = element + 1;
            if (next >= N_ELEMENTS) next = 0;


            for (int i = 0; i < N_GENES; i++) {

                int oldvalue = 0;
                elementLocks[element][i].lock();
                try {
                    oldvalue = GRID_ARRAY[element][i];
                } finally {
                    elementLocks[element][i].unlock();
                }


                int previousValue = 0;
                elementLocks[previous][i].lock();
                try {
                    previousValue = GRID_ARRAY[previous][i];
                } finally {
                    elementLocks[previous][i].unlock();
                }

                int nextValue = 0;
                elementLocks[next][i].lock();
                try {
                    nextValue = GRID_ARRAY[next][i];
                } finally {
                    elementLocks[next][i].unlock();
                }


                // Perform some kind of modeling calculation:
                int newvalue = oldvalue + previousValue + nextValue;
                newvalue /= 3; // Average the three values


                elementLocks[element][i].lock();
                try {
                    if (GRID_ARRAY[element][i] == oldvalue) {
                        GRID_ARRAY[element][i] = newvalue;
                    } else {
                        // Policy here to deal with failure. Here, we
                        // just report it and ignore it; our model
                        // will eventually deal with it.
                        System.out.println("Old value changed from " + oldvalue);
                    }
                } finally {
                    elementLocks[element][i].unlock();
                }
            }
        }
    }
//    @Benchmark
//    @Group("g")
//    @GroupThreads(1)
//    public int get() {
//        return counter.get();
//    }

    /*
     * ============================== HOW TO RUN THIS TEST: ====================================
     *
     * You will have the distinct metrics for inc() and get() from this run.
     *
     * You can run this test:
     *
     * a) Via the command line:
     *    $ mvn clean install
     *    $ java -jar target/benchmarks.jar JMHSample_15 -wi 5 -i 5 -f 1
     *    (we requested 5 warmup/measurement iterations, single fork)
     *
     * b) Via the Java API:
     *    (see the JMH homepage for possible caveats when running from IDE:
     *      http://openjdk.java.net/projects/code-tools/jmh/)
     */

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Exercise39.class.getSimpleName())
                .warmupIterations(3)
                .measurementIterations(3)
//                .warmupTime(TimeValue.seconds(10))
//                .measurementTime(TimeValue.seconds(10))
                .threads(N_EVOLVERS)
                .forks(1)
                .build();

        new Runner(opt).run();
    }

}
