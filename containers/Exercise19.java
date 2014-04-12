package containers;

import net.mindview.util.TextFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/27/12
 * Time: 6:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class Exercise19 {
    public static void main(String[] args) {
        String[] data = TextFile.read("Copyright.txt").split("\\W+");
        List<String> words = new ArrayList<String>(Arrays.asList(data));
        SimpleHashMap<String,Integer> wordOccurenceCounter =
                new SimpleHashMap<String, Integer>();
        String word;
        for(String tempWord: words) {
            word = tempWord.trim();
            if(!word.isEmpty()){
               int count = 0;
               for(String s: words)
                   if(s.equals(word))
                       count++;
               wordOccurenceCounter.put(word, count);
            }
        }
        System.out.println(wordOccurenceCounter);
    }
}
