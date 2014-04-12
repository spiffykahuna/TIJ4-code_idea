package containers.primefinder;

import java.util.ArrayList;

/**
 * starts at 3 and works up to maxPrime checking odd numbers for primeness.
 * To check if a number X is prime it checks for a remainder (modulus) when dividing.
 * It tries dividing X by all numbers from 3 to X/2, again odd numbers only.
 *
 */
public class SimplePG implements PrimeGenerator {

    private static ArrayList<Integer> getPrimes(int maxPrime) {

        ArrayList<Integer> primeList = new ArrayList<Integer>();

        /**
         * cycle through checking if a numbers prime.
         * To save time, only check odd numbers.
         * Since we are only checking odd numbers we only have to consider
         * odd multiples.
         */
        for (int i=2; i<=maxPrime;i++) {
            boolean isPrime = true;
            int highestFactorPossible = i/2;

            // SInce a number should only divide by 2 and itself.
            for(int j=2; j<i; j++) {
                if(i%j==0) {
                    isPrime = false;
                    break;
                }
            }

            if(isPrime) {
                primeList.add(i);
            }
        }

        return primeList;
    }

    public ArrayList<Integer> getPrimeList(int maxPrime) {
        return SimplePG.getPrimes(maxPrime);
    }
}
