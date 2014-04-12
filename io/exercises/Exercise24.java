package io.exercises;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2012-06-17
 * Time: 4:47 AM
 */
public class Exercise24 {
    private static final int BSIZE = 1024;
    public static void main(String[] args) {
        ByteBuffer bb = ByteBuffer.allocate(BSIZE);
        DoubleBuffer db = bb.asDoubleBuffer();
        // Store an array of int:
        db.put(new double[]{ 11.2, 42.3, 47.8, 99.0, 143.4, 811.2, 1016.7 });
        // Absolute location read and write:
        System.out.println(db.get(3));
        db.put(3, 1811.2);
        // Setting a new limit before rewinding the buffer.
        db.flip();
        while(db.hasRemaining()) {
            double i = db.get();
            System.out.println(i);
        }
    }
}
