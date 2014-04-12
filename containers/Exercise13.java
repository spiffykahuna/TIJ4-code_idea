package containers;


import net.mindview.util.TextFile;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/27/12
 * Time: 8:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class Exercise13 {
    public static void main(String[] args) {
        String[] data = TextFile.read("Copyright.txt").split("\\W+");
        List<String> words = new ArrayList<String>(Arrays.asList(data));
        AssociativeArray<String,Integer> wordOccurenceCounter =
                new AssociativeArray<String, Integer>(words.size());
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

//        // File whose words are to be counted:
//		String fileName = "Copyright.txt";
//		// Set of unique words in file:
//		Set<String> words = new TreeSet<String>(new TextFile(fileName, "\\W+"));
//		// Create initialize array of correct length:
//		AssociativeArray<String,Integer> wordCount =
//			new AssociativeArray<String,Integer>(words.size());
//		// Word list of all words in file:
//		ArrayList<String> fileList = new TextFile(fileName, "\\W+");
//		// Count appearances of each unique word and add to array:
//		for(String s : words) {
//			int count = 0;
//			for(String t : fileList) {
//				if(t.equals(s)) count++;
//			}
//			wordCount.put(s, count);
//		}
//		System.out.println(wordCount);

    }
}
