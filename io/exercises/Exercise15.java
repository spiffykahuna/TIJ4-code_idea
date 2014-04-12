package io.exercises;

import java.io.*;

import static net.mindview.util.PPrint.*;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2012-06-15
 * Time: 7:52 PM
 */
public class Exercise15 {
    static String fileName = "./io/exercises/Exercise15.java";
    static String outFile = "./io/exercises/Exercise15.out";
    public static void main(String[] args) throws IOException {
        DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(outFile)));
        System.out.println("Initial out.size() = " + out.size());
        byte[] ba = {0,1,2,3};
        // Store first 3 bytes of byte[] ba:
        out.write(ba,0,3);
        // Store all 4 bytes in byte[] ba:
        out.write(ba);
        out.write((int)255); // Stores the lower 8 bits of int
        out.writeBoolean(true);
        out.writeByte((int)1000000);
        out.writeBytes((String)"hi");
        out.writeChar(120);
        out.writeChars("hi");
        out.writeDouble(3.14159);
        out.writeFloat(2.1f);
        out.writeInt(1057);
        out.writeLong(123456789L);
        out.writeShort(123);
        out.writeUTF("Nice piece of work");
        System.out.println("After writing, out.size() = " + out.size());
        out.close();
        System.out.println("Reading:");
        DataInputStream in = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream(outFile)));
        byte[] baIn = new byte[3];
        System.out.println("bytes read by in.read(baIn, 0, 3) = " + in.read(baIn, 0, 3));
        System.out.print("baIn = ");
        for(int i = 0; i < baIn.length; i++)
            System.out.println(baIn[i] + " ");
        System.out.println();
        // Read next 4 bytes as int:
        System.out.println("in.readInt() = " + in.readInt());
        System.out.println("in.read() = " + in.read());
        System.out.println("in.readBoolean() = " + in.readBoolean());
        System.out.println("in.readByte() = " + in.readByte());
        System.out.println("in.read() = " + in.read()); // ASCII h = 104
        System.out.println("in.read() = " + in.read()); // ASCII i = 105
        System.out.println("in.readChar() = " + in.readChar());
        System.out.println("in.readChar() = " + in.readChar());
        System.out.println("in.readChar() = " + in.readChar());
        System.out.println("in.readDouble() = " + in.readDouble());
        System.out.println("in.readFloat() = " + in.readFloat());
        System.out.println("in.readInt() = " + in.readInt());
        System.out.println("in.readLong() = " + in.readLong());
        System.out.println("in.readShort() = " + in.readShort());
        System.out.println("in.readUTF() = " + in.readUTF());
    }
}
