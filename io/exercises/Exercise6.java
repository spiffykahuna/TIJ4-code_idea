package io.exercises;

import net.mindview.util.ProcessFiles;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2/5/12
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */

public class Exercise6 {
    public static void main(String[] args) {
        final Calendar dateBefore = Calendar.getInstance();
        dateBefore.set(2012,Calendar.JANUARY, 25);

        System.out.println("Looking for files that were modified after: " + dateBefore.getTime());


        new ProcessFiles(new ProcessFiles.Strategy() {
            public void process(File file) {
                Calendar modified = Calendar.getInstance();
                modified.setTimeInMillis(file.getAbsoluteFile().lastModified());
                if(modified.after(dateBefore))
                    System.out.println(file + "\t\t : Last modified: \t\t" + new Date(file.lastModified()) + "\n");
            }
        }, "java").start(args);
    }
}
