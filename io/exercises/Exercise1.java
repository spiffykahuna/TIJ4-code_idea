package io.exercises;

import net.mindview.util.TextFile;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2/5/12
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class Exercise1 {
    public static void main(final String[] args) {
        File path = new File(".");
        final String[] list;
        if(args.length == 0) {
            list = path.list();
            System.out.println(
                    "Usage: enter words, one or more of which each file must contain");
        }
        else {
            list = path.list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    // check for directory
                    File fileName = new File(name);
                    if(fileName.isDirectory())
                        return false;
                    return !(Collections.disjoint(
                            Arrays.asList(args),
                            new TextFile(name, "\\W+")
                    )
                    );
                }
            });
        }
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        for(String dirItem : list)
            System.out.println(dirItem);
    }
}
