package containers.primefinder;


import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2/2/12
 * Time: 12:55 PM
 * To change this template use File | Settings | File Templates.
 */



public class PrimeIntegerGenerator implements ListIterator<Integer> {
    static class Sieve {
        private BitSet sieve;

        public Sieve(int size) {
            sieve = new BitSet((size+1)/2);
        }

        public boolean is_composite(int k)
        {
            assert k >= 3 && (k % 2) == 1;
            return sieve.get((k-3)/2);
        }

        private void set_composite(int k)
        {
            assert k >= 3 && (k % 2) == 1;
            sieve.set((k-3)/2);
        }

        public static List<Integer> sieve_of_eratosthenes(int max)
        {
            Sieve sieve = new Sieve(max + 1); // +1 to include max itself
            for (int i = 3; i*i <= max; i += 2) {
                if (sieve.is_composite(i))
                    continue;

                // We increment by 2*i to skip even multiples of i
                for (int multiple_i = i*i; multiple_i <= max; multiple_i += 2*i)
                    sieve.set_composite(multiple_i);

            }
            List<Integer> primes = new ArrayList<Integer>(getAproxPrimesCount(max));
            primes.add(2);
            for (int i = 3; i <= max; i += 2)
                if (!sieve.is_composite(i))
                    primes.add(i);

            return primes;
        }

    }

    static int defaultMaxPrime = 10000;
    static boolean counting = false;

    private List<Integer> primeList;
    private ListIterator<Integer> lit;

    public PrimeIntegerGenerator() {
        primeList = Sieve.sieve_of_eratosthenes(defaultMaxPrime);
        lit = primeList.listIterator();
    }

    public PrimeIntegerGenerator(int maxPrime) {
        defaultMaxPrime = maxPrime;
        primeList = Sieve.sieve_of_eratosthenes(maxPrime);
        lit = primeList.listIterator();
    }
    public PrimeIntegerGenerator(int minPrime, int maxPrime) {
        defaultMaxPrime = maxPrime;
        primeList = getPrimesBetween(minPrime, maxPrime);
        lit = primeList.listIterator();
    }
    public PrimeIntegerGenerator(boolean countingGenerator) {
        this();
        counting =  countingGenerator;
    }


    public boolean hasNext() {
        if(counting  && !lit.hasNext()) {
            int currentIndex = lit.previousIndex() + 1;
            int nextPrime = findNextPrime(primeList.get(primeList.size() - 1));
            primeList.add(nextPrime);
            lit = primeList.listIterator(currentIndex);
        }
        return lit.hasNext();
    }

    public static int findNextPrime(Integer integer) {
        for(int i = integer+1; i < 2* integer; i++) {
            if(millerRabinPrime(i, Integer.MAX_VALUE) &&
                    nativePrime(i)) {
                return i;
            }
        }
        return -1;
    }

    public Integer next() {
        return lit.next();
    }

    public boolean hasPrevious() {
        return lit.hasPrevious();
    }

    public Integer previous() {
        return lit.previous();
    }

    public int nextIndex() {
        return lit.nextIndex();
    }

    public int previousIndex() {
        return lit.previousIndex();
    }

    public void remove() {
        throw new UnsupportedOperationException("No remove here");
    }

    public void set(Integer integer) {
        throw new UnsupportedOperationException("No set here");
    }

    public void add(Integer integer) {
        throw new UnsupportedOperationException("No add here");
    }

    public static List<Integer> getPrimes(int maxPrime) {
        return Sieve.sieve_of_eratosthenes(maxPrime);
    }
    public List<Integer> getPrimes() {
        return Sieve.sieve_of_eratosthenes(defaultMaxPrime);
    }

    @Deprecated
    private static List<Integer> getPrimesBitsetVersion(int maxPrime) {

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
            while(!primes.get(k)) {
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

    public static ArrayList<Integer> getPrimesBetween(int minPrime, int maxPrime) {

        int estimateNumPrimes = getAproxPrimesCount(maxPrime) - getAproxPrimesCount(minPrime) + 1;


        ArrayList<Integer> primeList = new ArrayList<Integer>(estimateNumPrimes);

        /**
         * cycle through checking if a numbers prime.
         * To save time, only check odd numbers.
         * Since we are only checking odd numbers we only have to consider
         * odd multiples.
         */
        for (int i=3; i<=maxPrime;i+=2) {
            if(i < minPrime) continue;
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

    /**
     * Determines if a number is probably prime using the Miller-Rabin primality
     * test.
     *
     * @param number
     * @param iterations
     *            How accurate the test needs to be. Accuracy ~= 1 -
     *            O(4^-iterations)
     * @return false if definitely composite. true if probably prime.
     */
    public static boolean millerRabinPrime(int number, int iterations)
    {
        if (number <= 1 || (number & 1) == 0)
        {
            if (number == 2)
            {
                return true;
            }
            // numbers less than or equal to 1 are not prime
            // even numbers are not prime
            return false;
        }
        else if (number == 3)
        {
            // 3 is prime
            return true;
        }
        // write number - 1 as 2^s * d, with d odd by factoring powers of 2 from
        // n-1
        long s = 1;
        while ((number - 1 & 1 << s) == 0)
        {
            ++s;
        }
        long d = (number - 1) / (1 << s);
        // System.out.println("2^" + s + " * " + d);
        Random generator = new Random();
        // if (iterations > number - 4)
        // {
        // iterations = number - 3;
        // }
        for (int i = 1; i <= iterations; ++i)
        {
            // pick a random integer a in the range [2, n-2]
            long a = generator.nextInt(number - 3) + 2;
            // test alternative: use an even a distribution
            // long a = (number - 3) * i / iterations;
            // compute x=a^d % number, check to see if x==1 or x==number-1
            long x = safe_pow(a, d, number);
            if (x == 1 || x == number - 1)
            {
                continue;
            }
            boolean gotoLoop = false;
            for (int r = 1; r < s && !gotoLoop; ++r)
            {
                // x = x^2 % n
                x = x * x % number;
                if (x == 1)
                {
                    return false;
                }
                else if (x == number - 1)
                {
                    gotoLoop = true;
                    break;
                }
            }
            if (!gotoLoop)
            {
                // definately composite
                return false;
            }
        }
        // probably prime
        return true;
    }

    /**
     * Computes base^power % mod. Protects against over-flow
     *
     * @param power
     * @param mod
     * @return  result of base^power % mod expression
     */
    public static long safe_pow(long base, long power, long mod)
    {
        if (power == 0)
        {
            return 1;
        }
        else if (power == 1)
        {
            return base;
        }
        else if ((power & 1) == 0)
        {
            // even power
            // base^power % mod = ((base * base % mod) ^ (power/2)) % mod
            return safe_pow((base * base % mod), power / 2, mod) % mod;
        }
        else
        {
            // odd
            // base^power % mod = ((base * base % mod) ^ (power/2) * base) % mod
            return safe_pow((base * base % mod), power / 2, mod) * base % mod;
        }
    }

    public static void nativeVsMillerRobin()
    {
        long start = System.currentTimeMillis();
        int startP = 0x0;
        int endP = 0x1FFFFF;
        for (int i = startP; i < endP; ++i)
        {
            nativePrime(i);
        }
        long end = System.currentTimeMillis();
        System.out.println("Low-range Naive: " + (end - start));
        System.out.println("Low-range Naive average: " + ((double) end - start) / (endP - startP));
        start = System.currentTimeMillis();
        for (int i = startP; i < endP; ++i)
        {
            millerRabinPrime(i, 0x5);
        }
        end = System.currentTimeMillis();
        System.out.println("Low-range miller-rabin: " + (end - start));
        System.out.println("Low-range miller-rabin average: " + ((double) end - start) / (endP - startP));
        for (int i = startP; i < endP; ++i)
        {
            boolean naive = nativePrime(i);
            boolean miller = millerRabinPrime(i, 0x5);
            if (naive != miller)
            {
                System.out.println("inaccurate for " + i + " naive: " + naive + " miller: " + miller);
            }
        }

        start = System.currentTimeMillis();
        startP = 0x7FF0FFFF;
        endP = 0x7FF2FFFF;
        for (int i = startP; i < endP; ++i)
        {
            nativePrime(i);
        }
        end = System.currentTimeMillis();
        System.out.println("High-range Naive: " + (end - start));
        System.out.println("High-range Naive average: " + ((double) end - start) / (endP - startP));
        start = System.currentTimeMillis();
        for (int i = startP; i < endP; ++i)
        {
            millerRabinPrime(i, 0x5);
        }
        end = System.currentTimeMillis();
        System.out.println("High-range miller-rabin: " + (end - start));
        System.out.println("High-range miller-rabin average: " + ((double) end - start) / (endP - startP));
        for (int i = startP; i < endP; ++i)
        {
            boolean naive = nativePrime(i);
            boolean miller = millerRabinPrime(i, 0x5);
            if (naive != miller)
            {
                System.out.println("inaccurate for " + i + " naive: " + naive + " miller: " + miller);
            }
        }
    }

    /**
     * Returns approximate primes count according to <b>maxPrime</b> parameter
     * ( uses algoritm described here <a href="http://primes.utm.edu/howmany.shtml">http://primes.utm.edu/howmany.shtml</a> )
     * @param maxPrime   max number to find
     * @return   prime numbers count
     */
    private static int getAproxPrimesCount(int maxPrime) {
        // for how this works see http://primes.utm.edu/howmany.shtml
        return (int) (1.02*(maxPrime / (Math.log(maxPrime)-1)));
    }

    public static boolean isPrime(int number) {
        if (number == 1)
            return false;
        if (number == 2)
            return true;
        if (number % 2 == 0)
            return false;
        for (int d = 3; d <= (int) Math.sqrt(number); d+=2)
            if (number % d == 0)
                return false;
        return true;
    }

    /**
     * Uses a naive primality test
     * @param number  number to test its primality
     * @return true if prime, false otherwise
     */
    public static boolean nativePrime(int number)
    {
        if (number <= 1 || (number & 1) == 0)
        {
//            if (number == 2)
//            {
//                return true;
//            }
//            return false;
            return number == 2;
        }
        int limit = (int) (Math.sqrt(number) + 1);
        for (int i = 3; i < limit; i += 2)
        {
            if (number % i == 0)
            {
                return false;
            }
        }
        return true;
    }



    public List<Integer> asList() {
        return primeList;
    }
    public String toString() {
        return primeList.toString();
    }
    public static void main(String[] args) {
        int max = (int) Math.floor(Math.pow(10, 8));
        System.out.println(new PrimeIntegerGenerator());
        List<Integer> between = PrimeIntegerGenerator.getPrimesBetween(4 , 20);
        System.out.println(between);
        System.out.println(new PrimeIntegerGenerator(4, 20));

        between = PrimeIntegerGenerator.getPrimesBetween(223243 , 223343);
        List<Integer> rightOne = PrimeIntegerGenerator.getPrimes(223343);
        System.out.println(between);
        rightOne.retainAll(between);
        System.out.println(rightOne);
        
        PrimeIntegerGenerator gen = new PrimeIntegerGenerator();
        while(gen.hasNext())
            System.out.println(gen.next());
        
        System.out.println(PrimeIntegerGenerator.isPrime(223313));
        System.out.println(PrimeIntegerGenerator.isPrime(gen.previous() + 1));

        PrimeIntegerGenerator.nativeVsMillerRobin();
        System.out.println(millerRabinPrime(Integer.MAX_VALUE - 2, Integer.MAX_VALUE));
        System.out.println(nativePrime(Integer.MAX_VALUE - 3124));
        System.out.println(isPrime(Integer.MAX_VALUE - 3243));
        between = new ArrayList<Integer> ();//PrimeIntegerGenerator.getPrimesBetween(Integer.MAX_VALUE - 2000 , Integer.MAX_VALUE-300);
        for(int i = Integer.MAX_VALUE - 2000; i < Integer.MAX_VALUE-300; i++ ) {
            if(millerRabinPrime(i, Integer.MAX_VALUE)) {
                between.add(i);
            }
        }

        System.out.println( between);
        System.out.println(isPrime(2147483237));
        System.out.println(nativePrime(2147481883));

        gen = new PrimeIntegerGenerator(true);
        int count = 1000;
        List<Integer> testList = new ArrayList<Integer>(count);
        while(gen.hasNext() && --count != 0) {
            int num = gen.next();
            testList.add(num);
            System.out.println(num);
        }
        List<Integer> testList2 =  new PrimeIntegerGenerator(7907).getPrimes();
        System.out.println(testList);
        System.out.println(testList2);
        System.out.println("Size() " + testList.size() + " = " + testList2.size());
        System.out.println("equals() " + testList.equals(testList2));


        System.out.println(new PrimeIntegerGenerator(16));
        System.out.println(Sieve.sieve_of_eratosthenes(18));
        System.out.println(Sieve.sieve_of_eratosthenes(19));
        System.out.println(Sieve.sieve_of_eratosthenes(23));
        System.out.println(Sieve.sieve_of_eratosthenes(24));
        System.out.println(PrimeIntegerGenerator.getPrimesBetween(4 , 18));



    }
}
