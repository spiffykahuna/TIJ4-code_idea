package containers.primefinder;

import java.util.List;


public interface PrimeGenerator {

    /**
     * Retrieve a list of prime numbers between 0 to maxPrime
     * @param maxPrime highest number to check for primeness, must be less than Integer.MAX_VALUE-2
     * @return list of all prime numbers between 0 and maxPrime
     */
    List<Integer> getPrimeList(int maxPrime);
}
