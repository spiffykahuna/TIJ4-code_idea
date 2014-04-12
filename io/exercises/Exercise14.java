package io.exercises;

import io.BufferedInputFile;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2012-06-15
 * Time: 7:32 PM
 */
public class Exercise14 {
    static String file = "./io/exercises/Exercise14_1.out";
    static String file2 = "./io/exercises/Exercise14_2.out";
    static String inputFile = "./io/exercises/Exercise14.java";
    public static void main(String[] args) throws IOException {

        BufferedReader in = new BufferedReader(
                new StringReader(
                        BufferedInputFile.read(inputFile)));
        // Buffered writer:
        PrintWriter out = new PrintWriter(
                new BufferedWriter(new FileWriter(file)));




        BufferedReader in2 = new BufferedReader(
                new StringReader(
                        BufferedInputFile.read(inputFile)));
        // Unbuffered writer:
        PrintWriter out2 = new PrintWriter(new FileWriter(file2));


        System.out.println("Buffered write:   " + getWriteDuration(in, out) + " nanoseconds");
        System.out.println("Unbuffered write: " + getWriteDuration(in2, out2) + " nanoseconds");
        // Show the stored files:
        System.out.println("file: \n" + BufferedInputFile.read(file));
        System.out.println("file2: \n" + BufferedInputFile.read(file2));
    }

    public static long getWriteDuration(BufferedReader in, PrintWriter out) throws IOException {
        int lineCount = 1;
        String s;
        long start = System.nanoTime();
        while((s = in.readLine()) != null )
            out.println(lineCount++ + ": " + s);
        out.close();
        return System.nanoTime() - start;
    }
}
