package io.exercises;

import net.mindview.util.TextFile;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2012-06-16
 * Time: 12:26 PM
 */
public class Exercise17 {
    static String fileName = "./io/exercises/Exercise17.java";
    public static void main(String[] args) {
        String fileContents = TextFile.read(fileName);
        Map<Character,Integer> counter = new TreeMap<Character, Integer>();

        for(Character c: fileContents.toCharArray()) {
            if (counter.containsKey(c) ) {
                Integer count = counter.get(c);
                counter.put(c, ++count);
            } else {
                counter.put(c,1);
            }
        }
        
        System.out.println("Result: ");
        for(Map.Entry<Character, Integer> entry: counter.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
