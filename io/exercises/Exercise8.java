package io.exercises;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2012-06-07
 * Time: 4:05 PM
 */
public class Exercise8 {
    public static void main(String[] args) {
        String filename =  "./io/exercises/Exercise8.java";

        try {
            if (args.length == 0) {
                new Exercise7.ReverseFileReader(filename).readFile();
            }else{
                for(String arg : args) {
                    System.out.println("Printing contents of the file: " + arg);
                    new Exercise7.ReverseFileReader(arg).readFile();
                    System.out.println("End of file" + arg);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
