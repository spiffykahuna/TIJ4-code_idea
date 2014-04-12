package containers.primefinder;

import java.util.ArrayList;

/**
 * Prime generator based on Sieve of Eratosthenes
 */
public class SievePG implements PrimeGenerator {

    private static ArrayList<Integer> getPrimes(int maxPrime) {

        ArrayList<Integer> primeList = null;
        boolean[] primes = new boolean[maxPrime+1];

        java.util.Arrays.fill(primes, true);
        primes[0] = false;
        primes[1] = false;
        primes[2] = true;
        int rootMP = (int) Math.floor(Math.sqrt(maxPrime));

        // get next prime
        for (int i=2; i<=rootMP;) {

            // use prime to amrk off all mutiples as not prime
            for(int j=(i*2); j<=maxPrime; j+=i) {
                primes[j] = false;
            }

            do {
                i++;
            } while(primes[i]==false);
            //System.out.println(i);
        }

        if(primeList==null) {
            primeList = new ArrayList<Integer>();
            for(int i=0; i<maxPrime; i++) {
                if(primes[i]) {
                    primeList.add(i);
                }
            }
        }
        return primeList;
    }

    public ArrayList<Integer> getPrimeList(int maxPrime) {
        return SievePG.getPrimes(maxPrime);
    }

    public static void main(String[] args) {
        System.out.println(new SievePG().getPrimeList(10000000).size());
    }
}
