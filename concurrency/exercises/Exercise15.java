package concurrency.exercises;

public class Exercise15 {

    interface SyncObject {
        void one();
        void two();
        void three();
    }

    static class SameObjectSynchronization implements SyncObject {
        Object lockOne = new Object();

        @Override
        public void one() {
            synchronized (lockOne) {
                for(int i = 0; i < 5; i++) {
                    System.out.println("one");
                    Thread.yield();
                }
            }
        }

        @Override
        public void two() {
            synchronized (lockOne) {
                for(int i = 0; i < 5; i++) {
                    System.out.println("two");
                    Thread.yield();
                }
            }
        }

        @Override
        public void three() {
            synchronized (lockOne) {
                for(int i = 0; i < 5; i++) {
                    System.out.println("three");
                    Thread.yield();
                }
            }
        }
    }

    static class SeparateObjectSynchronization implements SyncObject {
        Object lockOne = new Object();
        Object lockTwo = new Object();
        Object lockThree = new Object();

        @Override
        public void one() {
            synchronized (lockOne) {
                for(int i = 0; i < 5; i++) {
                    System.out.println("one");
                    Thread.yield();
                }
            }
        }

        @Override
        public void two() {
            synchronized (lockTwo) {
                for(int i = 0; i < 5; i++) {
                    System.out.println("two");
                    Thread.yield();
                }
            }
        }

        @Override
        public void three() {
            synchronized (lockThree) {
                for(int i = 0; i < 5; i++) {
                    System.out.println("three");
                    Thread.yield();
                }
            }
        }
    }

    static class SyncTest {
        static void run(final SyncObject syncObject) {
            System.out.println("===============================================================");

            Thread t1 = new Thread() {
                @Override
                public void run() {
                    syncObject.one();
                }
            };
            Thread t2 =  new Thread() {
                @Override
                public void run() {
                    syncObject.two();
                }
            };

            Thread t3 = new Thread() {
                @Override
                public void run() {
                    syncObject.three();
                }
            };

            t1.start(); t2.start(); t3.start();

            try {
                t3.join(); t2.join(); t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("===============================================================");
        }
    }

    public static void main(String[] args) {
        SyncTest.run(new SameObjectSynchronization());
        SyncTest.run(new SeparateObjectSynchronization());
    }
}
