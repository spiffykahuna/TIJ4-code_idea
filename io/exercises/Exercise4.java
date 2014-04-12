package io.exercises;

import net.mindview.util.Directory;

import java.io.File;

import static net.mindview.util.Print.print;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2/5/12
 * Time: 6:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Exercise4 {
    public static void main(String[] args) {
        long sum = 0;
        for(File file : Directory.walk("/home/deko/Desktop", ".*\\.png")) {
           System.out.println(file + " : " + file.length());
           sum+= file.length();
        }
        System.out.println("Koda na : " + sum );
    }
}
