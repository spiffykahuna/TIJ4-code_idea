package io.exercises;

import net.mindview.util.TextFile;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2013-12-16
 * Time: 12:30 AM
 */
public class Exercise32 {
    public static void main(String[] args) throws IOException {
        List<String> words = new TextFile("io/exercises/Exercise32.java", "\\W+");

        Map<String,Integer> wordCount = countWords(words);

        Element root = getWordXml(wordCount);

        printXML(root);

    }

    private static void printXML(Element root) throws IOException {
        Document doc = new Document(root);

        Serializer serializer= new Serializer(System.out);
        serializer.setIndent(4);
        serializer.setMaxLength(60);
        serializer.write(doc);
        serializer.flush();
    }

    private static Element getWordXml(Map<String, Integer> wordCount) {
        Element root = new Element("occurrences");

        for(Map.Entry<String, Integer> occurrence: wordCount.entrySet()) {
            String word = occurrence.getKey();
            int count = occurrence.getValue();

            Element wordElement = new Element("word");
            wordElement.appendChild(word);

            Element countElement = new Element("count");
            countElement.appendChild(String.valueOf(count));

            Element occurrenceElement = new Element("occurrence");
            occurrenceElement.appendChild(wordElement);
            occurrenceElement.appendChild(countElement);

            root.appendChild(occurrenceElement);
        }

        return root;
    }

    private static Map<String, Integer> countWords(List<String> words) {
        Map<String, Integer> wordCount = new HashMap<String, Integer>();

        for(String word: words) {
            if(wordCount.containsKey(word)) {
                int count = wordCount.get(word);
                wordCount.put(word, ++count);
            } else {
                wordCount.put(word, 1);
            }
        }

        return wordCount;
    }
}

