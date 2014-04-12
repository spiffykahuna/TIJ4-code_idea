package containers.primefinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Check the speed of the various PrimeGenerators
 */
public class SpeedTest {

    public static void main(String[] args) {

        long startTime;
        long estimatedTime;
        List<Integer> primeNums;
        int max = (int) Math.pow(10, 8); //Integer.MAX_VALUE/(8*8*8*8*2);
        long sum = 0;

        System.out.println("Timning generating list of primes between 0 to " + max);

        /******** Baseline  ***********/
        System.out.println("\nBaseline");
        // real baseline
        primeNums = new SievePG().getPrimeList(max);
        primeNums = new SievePG().getPrimeList(max);
        startTime = System.nanoTime();
        primeNums = new SievePG().getPrimeList(max);
        for(int p : primeNums) {
            sum+=p;
        }
        estimatedTime = System.nanoTime() - startTime;
        long baseTime = estimatedTime;

        /***** Setup ready to run all Prime Generators  *********/
        ArrayList<PrimeGenerator> pgs = new ArrayList<PrimeGenerator>();
        pgs.add(new BitSetOddSkippingSievePG());
        pgs.add(new OddSkippingSievePG());
        pgs.add(new SkippingSievePG());
        pgs.add(new SievePG());
//        pgs.add(new SimplePG());
//        pgs.add(new SimpleOddPG());

        /***************************/
        for(PrimeGenerator pg : pgs) {
            System.gc();
            System.gc();
            System.gc();
            System.gc();
            System.out.println("---" + pg.getClass().getName());
            startTime = System.nanoTime();
            primeNums = pg.getPrimeList(max);
            sum = 0;
            for(int p : primeNums) {
                sum+=p;
            }
            estimatedTime = System.nanoTime() - startTime;
            long estimatedMem = Runtime.getRuntime().totalMemory() -  Runtime.getRuntime().freeMemory();

            System.out.print("est Time = " + ((double)estimatedTime/(double)baseTime));
            System.out.print("est Time = " + estimatedTime/1000000);
            System.out.println(", " + primeNums.size() + " primes sum=" + sum);
        }

    }
}
