package io.exercises;

import net.mindview.util.BinaryFile;

import java.io.IOException;
import java.util.Formatter;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2012-06-16
 * Time: 1:23 PM
 */
public class Exercise19 {
    static String fileName = "./out/production/TIJ4-code_idea/io/exercises/Exercise19.class";
    public static void main(String[] args) {
        try {
            byte[] fileBytes = BinaryFile.read(fileName);
            Map<Byte,Integer> counter = new TreeMap<Byte, Integer>();

            for(Byte b: fileBytes) {
                if (counter.containsKey(b) ) {
                    Integer count = counter.get(b);
                    counter.put(b, ++count);
                } else {
                    counter.put(b,1);
                }
            }

            System.out.println("Result: ");
            for(Map.Entry<Byte, Integer> entry: counter.entrySet()) {
                System.out.println(String.format("%02X", entry.getKey()) + " : " + entry.getValue());
            }

        } catch(IOException e) {
            System.err.println("Caught " + e);
            e.printStackTrace();
        }

    }

}
