package io.exercises;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2/5/12
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */

class SortedDirList {
    private File directory;
    private String[] list;

    public SortedDirList(File dir) {
        this.directory = dir;
        list = dir.list();
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
    }

    public static String[] list(File dir) {
        String[] list = dir.list();
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    public static String[] list(File dir, final String regexPattern) {
        String[] list =  dir.list(new FilenameFilter() {
            private Pattern pattern = Pattern.compile(regexPattern);
            public boolean accept(File dir, String name) {
                return pattern.matcher(name).matches();
            }

        });
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    @Override
    public String toString() {
        return (list == null ? null : Arrays.asList(list).toString());
    }
}

public class Exercise2 {
    public static void main(String[] args) {
        File testPath = new File("/home/deko/Desktop");
        System.out.println(new SortedDirList(testPath));
        System.out.println(Arrays.asList(SortedDirList.list(testPath)));
        System.out.println(Arrays.asList(SortedDirList.list(testPath, "^.*.png$")));
    }
}
