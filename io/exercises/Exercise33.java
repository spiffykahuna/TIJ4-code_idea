package io.exercises;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.prefs.Preferences;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2013-12-16
 * Time: 12:54 AM
 */
public class Exercise33 {
    public static void main(String[] args) {
        Preferences prefs = Preferences
                .userNodeForPackage(Exercise33.class);
        int value = prefs.getInt("base directory", 0);
        System.out.print("Base directory value = " + value +
                "\nEnter new base directory value (integer): ");

        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        try {
            value = Integer.parseInt(br.readLine());
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        prefs.putInt("base directory", value);
    }
}
