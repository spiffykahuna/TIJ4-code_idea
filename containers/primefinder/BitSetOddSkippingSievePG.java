/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package containers.primefinder;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Prime generator based on Sieve of Eratosthenes {@link SievePrimeGenerator},
 * improve speed/memory making the boolean array represent odd numbers instead of
 * all numbers. Then use the ideas from {@link ISievePrimeGenerator}. ie:
 * When sieving using the prime 3 instead of sieving 6,9,12,15,18,21 we
 * can actually sieve 9,15,21 ie jump double our prime.
 * each time.
 */
public class BitSetOddSkippingSievePG implements PrimeGenerator {

    private static ArrayList<Integer> getPrimes(int maxPrime) {

        // for how this works see http://primes.utm.edu/howmany.shtml
        int estimateNumPrimes = (int) (1.02*(maxPrime / (Math.log(maxPrime)-1)));
        ArrayList<Integer> primeList = new ArrayList<Integer>(estimateNumPrimes);

        /**
         * array where index represents odd number, eg if primes[x] = true
         * then (x*2)+1 is prime.
         */
        BitSet primes = new BitSet(maxPrime/2+1);
        primes.set(0, primes.size(), true);

        primes.set(0, false);
        primes.set(1, true);
        primeList.add(2);
        int rootMP = (int) Math.floor(Math.sqrt(maxPrime));
        int halfMax = maxPrime/2;

        // get next prime
        for (int i=3; i<=rootMP;) {
            // use prime to amrk off all mutiples as not prime
            for(int j=((i*3)/2); j<=halfMax; j+=i) {
                primes.set(j, false);
            }

            // just want to move up index's while not prime
            // rolled out like this to prevent needing division to get index
            i+=2;
            int k = i/2;
            while(primes.get(k)==false) {
                k++;
            }
            i = (k*2)+1;
        }

        // the index's represent odd numbers starting with 1
        for(int i=0; i<=halfMax; i++) {
            if(primes.get(i)) {
                primeList.add((i*2)+1);
            }
        }

        return primeList;
    }

    public ArrayList<Integer> getPrimeList(int maxPrime) {
        return getPrimes(maxPrime);
    }

    public static void main(String[] args) {
        System.out.println(new BitSetOddSkippingSievePG().getPrimeList(10000000).size());
    }
}
