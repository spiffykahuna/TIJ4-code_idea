package io.exercises;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2012-06-07
 * Time: 4:44 PM
 */
public class Exercise10 {
    public static class ReverseFileReaderMatcher {
        protected String filePath;
        protected String searchPattern;
        protected BufferedReader in;
        protected List<String> lines;
        protected String s;

        public ReverseFileReaderMatcher(String filePath, String regex) {
            this.filePath = filePath;
            this.lines = new ArrayList<String>();
            this.searchPattern = regex;
        }

        public void readFile() throws IOException {
            this.in = new BufferedReader(
                    new FileReader(new File(filePath).getCanonicalFile()));
            while((s = in.readLine())!= null)
                if (s.contains(searchPattern))
                    lines.add(s + "\n");
            in.close();
            //Collections.sort(lines, Collections.reverseOrder());
            ListIterator lit = lines.listIterator(lines.size());
            while(lit.hasPrevious())
                System.out.println(lit.previous());
        }

    }

    public static void main(String[] args) {
        String filename =  "./io/exercises/Exercise10.java";
        try {
            if (args.length == 0) {
                new ReverseFileReaderMatcher(filename, "").readFile();
            }else{
                filename = args[0];
                for(String arg : Arrays.copyOfRange(args, 1, args.length)) {
                    System.out.println("Printing lines that contain: " + arg);
                    new ReverseFileReaderMatcher(filename, arg).readFile();
                    System.out.println("End Of listing");
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
}
