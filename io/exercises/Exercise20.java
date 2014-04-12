package io.exercises;

import net.mindview.util.BinaryFile;
import net.mindview.util.Directory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2012-06-16
 * Time: 1:38 PM
 */
public class Exercise20 {
    public static void main(String[] args) {
        boolean allFilesStartWithCAFEBABE = true;
        String path = "";
        try {
            path = new File("./").getCanonicalPath();

            Directory.TreeInfo tree = Directory.walk("./", ".*\\.class");
            for(File file: tree.files) {
               byte[] fileBytes = BinaryFile.read(file.getCanonicalFile());
               String hexString = bytesToHexString(Arrays.copyOf(fileBytes, 4));

               if(!hexString.toUpperCase().equals("CAFEBABE"))
                   allFilesStartWithCAFEBABE = false;

               System.out.println("Filename: " + file.getName() + " startsWith: " + hexString);
            }
        } catch(IOException e) {
            System.err.println("Caught " + e);
            e.printStackTrace();
        }

        System.out.println("All files in " + path + " start with  ‘CAFEBABE’ ? : "
                + (allFilesStartWithCAFEBABE ? "Yes": "No") );

    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return sb.toString();
    }
}
