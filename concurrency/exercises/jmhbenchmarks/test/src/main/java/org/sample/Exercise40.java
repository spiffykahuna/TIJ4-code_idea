package org.sample;


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
Exercise 40: (6) Following the example of ReaderWriterList.java, create a
ReaderWriterMap using a HashMap. Investigate its performance by modifying
MapComparisons.java. How does it compare to a synchronized HashMap and a
ConcurrentHashMap?
 */
public class Exercise40 {

    private final static int MAP_SIZE = 1000;

    private final static int READERS = 100;
    private final static int WRITERS = 10;

    private final static Random rand = new Random(47);

    private final static Map<Integer, String> testDataMap = createRandomMap();

    private static Map<Integer, String> createRandomMap() {
        Map<Integer, String> testDataMap = new HashMap<Integer, String>(MAP_SIZE);
        for(int i = 0; i < MAP_SIZE; i++) {
            int strLen =  rand.nextInt(MAP_SIZE);
            StringBuilder sb = new StringBuilder(strLen);
            for(int k = 0; k < strLen; k++) {
                sb.append(new Character((char) rand.nextInt(1024)));
            }
            testDataMap.put(i, sb.toString());
        }
        return testDataMap;
    }


    public static class ReaderWriterLockedHashMap<K, V> {
        private Map<K, V> lockedMap;
        // Make the ordering fair:
        private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

        public ReaderWriterLockedHashMap(int size, Map<K, V> initialValue) {
            lockedMap = new HashMap<K, V>(initialValue);
        }

        public V put(K key, V value) {
            Lock wlock = lock.writeLock();
            wlock.lock();
            try {
                return lockedMap.put(key, value);
            } finally {
                wlock.unlock();
            }
        }

        public V get(K key) {
            Lock rlock = lock.readLock();
            rlock.lock();
            try {
                // Show that multiple readers
                // may acquire the read lock:
                if (lock.getReadLockCount() > 1)
                    System.out.println(lock.getReadLockCount());
                return lockedMap.get(key);
            } finally {
                rlock.unlock();
            }
        }
    }

    @State(Scope.Group)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public static class ReaderWriterLockedHashMapBenchmark {
        ReaderWriterLockedHashMap<Integer, String> readerWriterLockedHashMap;
        private Random rand = new Random(47);

        @Setup
        public void setup() {
            readerWriterLockedHashMap = new ReaderWriterLockedHashMap<Integer, String>(MAP_SIZE, createRandomMap());
        }

        @Benchmark
        @Group("lockedHashMap")
        @GroupThreads(READERS)
        public String read() {
            return readerWriterLockedHashMap.get(rand.nextInt(MAP_SIZE));
        }

        @Benchmark
        @Group("lockedHashMap")
        @GroupThreads(WRITERS)
        public String write() {
            return readerWriterLockedHashMap.put(rand.nextInt(), testDataMap.get(rand.nextInt(MAP_SIZE)));
        }
    }



    public static class ReaderWriterSynchronizedHashMap<K, V> {
        private Map<K, V> lockedMap;

        public ReaderWriterSynchronizedHashMap(int size, Map<K, V> initialValue) {
            lockedMap = new HashMap<K, V>(initialValue);
        }

        public synchronized V put(K key, V value) {
            return lockedMap.put(key, value);
        }

        public synchronized V get(K key) {
            return lockedMap.get(key);
        }
    }

    @State(Scope.Group)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public static class ReaderWriterSynchronizedHashMapBenchmark {
        ReaderWriterSynchronizedHashMap<Integer, String> readerWriterSynchronizedHashMap;
        private Random rand = new Random(47);

        @Setup
        public void setup() {
            readerWriterSynchronizedHashMap = new ReaderWriterSynchronizedHashMap<Integer, String>(MAP_SIZE, createRandomMap());
        }

        @Benchmark
        @Group("synchronizedHashMap")
        @GroupThreads(READERS)
        public String read() {
            return readerWriterSynchronizedHashMap.get(rand.nextInt(MAP_SIZE));
        }

        @Benchmark
        @Group("synchronizedHashMap")
        @GroupThreads(WRITERS)
        public String write() {
            return readerWriterSynchronizedHashMap.put(rand.nextInt(), testDataMap.get(rand.nextInt(MAP_SIZE)));
        }
    }


    public static class ReaderWriterConcurrentHashMap<K, V> {
        private Map<K, V> lockedMap;

        public ReaderWriterConcurrentHashMap(int size, Map<K, V> initialValue) {
            lockedMap = new ConcurrentHashMap<K, V>(initialValue);
        }

        public V put(K key, V value) {
            return lockedMap.put(key, value);
        }

        public V get(K key) {
            return lockedMap.get(key);
        }
    }

    @State(Scope.Group)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public static class ReaderWriterConcurrentHashMapBenchmark {
        private ReaderWriterConcurrentHashMap<Integer, String> readerWriterConcurrentHashMap;
        private Random rand = new Random(47);

        @Setup
        public void setup() {
            readerWriterConcurrentHashMap = new ReaderWriterConcurrentHashMap<Integer, String>(MAP_SIZE, createRandomMap());
        }

        @Benchmark
        @Group("concurrentHashMap")
        @GroupThreads(READERS)
        public String read() {
            return readerWriterConcurrentHashMap.get(rand.nextInt(MAP_SIZE));
        }

        @Benchmark
        @Group("concurrentHashMap")
        @GroupThreads(WRITERS)
        public String write() {
            return readerWriterConcurrentHashMap.put(rand.nextInt(), testDataMap.get(rand.nextInt(MAP_SIZE)));
        }
    }




    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Exercise40.class.getSimpleName())
                .warmupIterations(10)
                .measurementIterations(10)
//                .warmupTime(TimeValue.seconds(10))
//                .measurementTime(TimeValue.seconds(10))
//                .threads(N_EVOLVERS)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}

