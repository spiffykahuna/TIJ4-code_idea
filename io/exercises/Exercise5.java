package io.exercises;

import net.mindview.util.Directory;


import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2/5/12
 * Time: 6:19 PM
 * To change this template use File | Settings | File Templates.
 */
class ProcessFilesWithRegexp {
    public interface Strategy {
        void process(File file);
    }
    private Strategy strategy;
    private String regex;
    private Pattern pattern;

    public ProcessFilesWithRegexp(Strategy strategy, String regex) {
        this.strategy = strategy;
        this.regex = regex;
        pattern = Pattern.compile(regex);
    }

    public void start(String[] args) {
        try {
            if(args.length == 0)
                processDirectoryTree(new File("."));
            else
                for(String arg : args) {
                    File fileArg = new File(arg);
                    if(fileArg.isDirectory())
                        processDirectoryTree(fileArg);
                    else {
                        // Allow user to leave off extension:
                        if(pattern.matcher(arg).matches())
                            strategy.process(
                                    new File(arg).getCanonicalFile());
                    }
                }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void
    processDirectoryTree(File root) throws IOException {
        for(File file : Directory.walk(
                root.getAbsolutePath(), regex))
            strategy.process(file.getCanonicalFile());
    }
    // Demonstration of how to use it:
    public static void main(String[] args) {
        new ProcessFilesWithRegexp(new ProcessFilesWithRegexp.Strategy() {
            public void process(File file) {
                System.out.println(file);
            }
        }, ".*\\.java").start(args);
    }

}
public class Exercise5 {
    public static void main(String[] args) {
        String[] arg = new String[0];
        ProcessFilesWithRegexp.main(arg);
    }
}
