package io.exercises;


import java.io.*;
import java.util.ArrayList;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2/5/12
 * Time: 7:52 PM
 * To change this template use File | Settings | File Templates.
 */

public class Exercise7 {
    public static class ReverseFileReader {
        protected String filePath;
        protected BufferedReader in;
        protected List<String> lines;
        protected String s;
        
        public ReverseFileReader(String filePath) {
            this.filePath = filePath;
            this.lines = new ArrayList<String>();

        }

        public void readFile() throws IOException {
            this.in = new BufferedReader(
                    new FileReader(new File(filePath).getCanonicalFile()));
            while((s = in.readLine())!= null)
                lines.add(s + "\n");
            in.close();
            //Collections.sort(lines, Collections.reverseOrder());
            ListIterator lit = lines.listIterator(lines.size());
            while(lit.hasPrevious())
                System.out.println(lit.previous());
        }

        public List<String> getReversedLines() {
            List<String> result = new LinkedList<String>();
            ListIterator lit = lines.listIterator(lines.size());
            while(lit.hasPrevious())
                result.add((String) lit.previous());
            return result;
        }

    }
    public static void main(String[] args) throws IOException {
        String filename =  "./io/exercises/Exercise7.java";
        new ReverseFileReader(filename).readFile();
    }
}
