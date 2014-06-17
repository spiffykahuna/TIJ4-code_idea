package concurrency.exercises;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Exercise16 {
    static class SingleExplicitLockSyncObject implements Exercise15.SyncObject {
        private Lock lock = new ReentrantLock();

        @Override
        public void one() {
            lock.lock();
            try {
                for(int i = 0; i < 5; i++) {
                    System.out.println("one");
                    Thread.yield();
                }
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void two() {
            lock.lock();
            try {
                for(int i = 0; i < 5; i++) {
                    System.out.println("two");
                    Thread.yield();
                }
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void three() {
            lock.lock();
            try {
                for(int i = 0; i < 5; i++) {
                    System.out.println("three");
                    Thread.yield();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    static class MultipleExplicitLockSyncObject implements Exercise15.SyncObject {
        private Lock lockOne = new ReentrantLock();
        private Lock lockTwo = new ReentrantLock();
        private Lock lockThree = new ReentrantLock();

        @Override
        public void one() {
            lockOne.lock();
            try {
                for(int i = 0; i < 5; i++) {
                    System.out.println("one");
                    Thread.yield();
                }
            } finally {
                lockOne.unlock();
            }
        }

        @Override
        public void two() {
            lockTwo.lock();
            try {
                for(int i = 0; i < 5; i++) {
                    System.out.println("two");
                    Thread.yield();
                }
            } finally {
                lockTwo.unlock();
            }
        }

        @Override
        public void three() {
            lockThree.lock();
            try {
                for(int i = 0; i < 5; i++) {
                    System.out.println("three");
                    Thread.yield();
                }
            } finally {
                lockThree.unlock();
            }
        }
    }

    public static void main(String[] args) {
        Exercise15.SyncTest.run(new SingleExplicitLockSyncObject());
        Exercise15.SyncTest.run(new MultipleExplicitLockSyncObject());
    }
}
