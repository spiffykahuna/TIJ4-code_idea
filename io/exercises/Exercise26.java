package io.exercises;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2012-06-17
 * Time: 10:14 PM
 */
public class Exercise26 {
    public static void main(String[] args) throws Exception {
        if(args.length < 2) {
            System.out.println("Usage: java JGrep26 file regex");
            System.exit(0);
        }
        FileChannel fc = new FileInputStream(args[0]).getChannel();
        MappedByteBuffer in =
                fc.map(FileChannel.MapMode.READ_ONLY, 0,
                        new File(args[0]).length());
        // Decode bytes to chars and create array of 'lines':
        String[] sa = Charset.forName(System.getProperty("file.encoding"))
                .decode(in).toString().split("\n");
        Pattern p = Pattern.compile(args[1]);
        Matcher m = p.matcher(""); // creates emtpy Matcher object
        int index = 0;
        for(String line : sa) {
            m.reset(line);
            while(m.find())
                System.out.println(index++ + ": " +
                        m.group() + ": " + m.start());
        }
        fc.close();

    }
}
