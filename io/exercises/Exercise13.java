package io.exercises;

import io.BufferedInputFile;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2012-06-15
 * Time: 7:21 PM
 */
public class Exercise13 {
    static String file = "./io/exercises/Exercise13.out";

    public static void main(String[] args)
            throws IOException {
        BufferedReader in = new BufferedReader(
                new StringReader(
                        BufferedInputFile.read("./io/exercises/Exercise13.java")));

        LineNumberReader lineReader = new LineNumberReader(in);

        PrintWriter out = new PrintWriter(
                new BufferedWriter(new FileWriter(file)));

        String s;
        while((s = lineReader.readLine()) != null )
            out.println((lineReader.getLineNumber()) + ": " + s);
        out.close();
        // Show the stored file:
        System.out.println(BufferedInputFile.read(file));
    }
}
