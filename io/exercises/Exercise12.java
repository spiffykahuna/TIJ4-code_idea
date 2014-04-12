package io.exercises;

import net.mindview.util.TextFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2012-06-15
 * Time: 6:55 PM
 */
public class Exercise12 {
    public static void main(String[] args) {
        String filename =  "./io/exercises/Exercise12.java";

        try {
            if (args.length == 0) {
                new Exercise7.ReverseFileReader(filename).readFile();
            }else{
                for(String arg : args) {
                    System.out.println("Printing contents of the file: " + arg);
                    Exercise7.ReverseFileReader file = new Exercise7.ReverseFileReader(arg);
                    file.readFile();

                    List<String> reversedLines = file.getReversedLines();
                    ListIterator<String> lit = reversedLines.listIterator(reversedLines.size());

                    StringBuilder outText = new StringBuilder();
                    int i = 1;
                    while (lit.hasPrevious())
                        outText.append("Line " + (i++) + ": " + lit.previous());

                    PrintWriter out = new PrintWriter("./io/exercises/Exercise12.out");
                    out.write(outText.toString());
                    out.close();

                    System.out.println("End of file" + arg);

                    System.out.println("Contents of resultFile: \n\n");
                    for(String line: new TextFile("./io/exercises/Exercise12.out")) {
                        System.out.println(line);
                    };

                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
}
