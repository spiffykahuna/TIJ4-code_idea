package containers.primefinder;

import java.util.ArrayList;

/**
 * starts at 3 and works up to maxPrime checking odd numbers for primeness.
 * To check if a number X is prime it checks for a remainder (modulus) when dividing.
 * It tries dividing X by all numbers from 3 to X/2, again odd numbers only.
 *
 */
public class SimpleOddPG implements PrimeGenerator {

    private static ArrayList<Integer> getPrimes(int maxPrime) {

        ArrayList<Integer> primeList = new ArrayList<Integer>();
        primeList.add(2);

        /**
         * cycle through checking if a numbers prime.
         * To save time, only check odd numbers.
         * Since we are only checking odd numbers we only have to consider
         * odd multiples.
         */
        for (int i=3; i<=maxPrime;i+=2) {
            boolean isPrime = true;
            int highestFactorPossible = i/2;

            for(int j=3; j<highestFactorPossible; j+=2) {
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
        return SimpleOddPG.getPrimes(maxPrime);
    }


    public static void main(String[] args) {
        System.out.println(new SimpleOddPG().getPrimeList(1000000).size());
    }
}
