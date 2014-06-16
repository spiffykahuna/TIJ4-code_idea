package concurrency.exercises;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Exercise11 {
    String firstPart;
    String secondPart;

    String initialWord;

    public Exercise11(String word) {
        initialWord = word.toLowerCase();
        init();
    }

    private void init() {
        firstPart = initialWord.substring(0, initialWord.length() / 2);
        secondPart = initialWord.substring(initialWord.length() / 2);
    }

    public synchronized String getInitialWord() {
        return initialWord;
    }

//    public String transform() {
    public synchronized String transform() {
        String first = firstPart;
        String second = secondPart;

        first = toggleCase(first);
        firstPart = first;
//        Thread.yield();
        second = toggleCase(second);
        secondPart = second;


        String result = firstPart + secondPart;
        init(); // reset initial state
        return result;
    }

    private static String toggleCase(String word) {
        return word.toLowerCase().equals(word) ? word.toUpperCase() : word.toLowerCase();
    }

    static class UpperCaseTransformer implements Runnable {
        final Exercise11 wordResource;
        final String properResult;

        public UpperCaseTransformer(Exercise11 wordResource) {
            this.wordResource = wordResource;
            this.properResult = wordResource.getInitialWord().toUpperCase();
        }

        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                String result = wordResource.transform();

                if(!result.equals(properResult)) {
                    System.out.println(
                            String.format("Transformation is wrong: should_be=%s result=%s", properResult, result)
                    );
                }
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        Exercise11 wordResource = new Exercise11("internationalization");
        for (int i = 0; i < 1000; i++) {
            exec.execute(new UpperCaseTransformer(wordResource));
        }
        exec.shutdown();
    }
}
