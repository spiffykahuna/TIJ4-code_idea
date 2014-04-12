/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package containers.primefinder;

import java.util.ArrayList;

/**
 * Prime generator based on Sieve of Eratosthenes {@link SievePrimeGenerator},
 * improve speed by taking advantage
 * that even numbers are never prime except 2. Therefore when sieving using the prime 3,
 * instead of sieving 6,9,12,15,18,21 we can actually sieve 9,15,21 ie jump double our prime
 * each time. Then when checking over the boolean array created again jump using
 * increments of 2.
 */
public class SkippingSievePG implements PrimeGenerator {

    private static ArrayList<Integer> getPrimes(int maxPrime) {

        ArrayList<Integer> primeList = new ArrayList<Integer>();

        /**
         * array where index represents its own number, eg if primes[x] = true
         * then x is prime.
         */
        boolean[] primes = new boolean[maxPrime+1];

        java.util.Arrays.fill(primes, true);
        primes[0] = false;
        primes[1] = false;
        primes[2] = true;
        primeList.add(2);
        int rootMP = (int) Math.floor(Math.sqrt(maxPrime));

        // get next prime
        for (int i=3; i<=rootMP;) {
            // use prime to amrk off all mutiples as not prime
            int dI = i*2;
            for(int j=(i*3); j<=maxPrime; j+=dI) {
                primes[j] = false;
            }

            do {
                i+=2;
            } while(primes[i]==false);
        }

        for(int i=3; i<=maxPrime; i+=2) {
            if(primes[i]) {
                primeList.add(i);
            }
        }

        return primeList;
    }

    public ArrayList<Integer> getPrimeList(int maxPrime) {
        return getPrimes(maxPrime);
    }


    public static void main(String[] args) {
        System.out.println(new SkippingSievePG().getPrimeList(10000000).size());
    }
}
