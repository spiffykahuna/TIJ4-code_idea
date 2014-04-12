package io.exercises;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2012-06-07
 * Time: 4:17 PM
 */
public class Exercise9 {
    public static class ReverseFileReaderUpper extends Exercise7.ReverseFileReader {

        public ReverseFileReaderUpper(String filePath) {
            super(filePath);
        }

        @Override
        public void readFile() throws IOException {
            this.in = new BufferedReader(
                    new FileReader(new File(filePath).getCanonicalFile()));
            while((s = in.readLine())!= null)
                lines.add(s.toUpperCase() + "\n");
            in.close();
            //Collections.sort(lines, Collections.reverseOrder());
            ListIterator lit = lines.listIterator(lines.size());
            while(lit.hasPrevious())
                System.out.println(lit.previous());
        }
    }
    public static void main(String[] args) {
        String filename =  "./io/exercises/Exercise9.java";

        try {
            if (args.length == 0) {
                new ReverseFileReaderUpper(filename).readFile();
            }else{
                for(String arg : args) {
                    System.out.println("Printing contents of the file: " + arg);
                    new ReverseFileReaderUpper(arg).readFile();
                    System.out.println("End of file" + arg);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
