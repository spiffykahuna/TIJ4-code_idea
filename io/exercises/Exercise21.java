package io.exercises;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2012-06-16
 * Time: 2:13 PM
 */
public class Exercise21 {
    public static void main(String[] args)
            throws IOException {
        BufferedReader stdin = new BufferedReader(
                new InputStreamReader(System.in));
        String s;
        while((s = stdin.readLine()) != null)
            System.out.println(s.toUpperCase());
        // An empty line or Ctrl-Z terminates the program
    }
}
