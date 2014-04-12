package io.exercises;

import net.mindview.util.TextFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static net.mindview.util.Print.print;
import static net.mindview.util.Print.printnb;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2012-06-17
 * Time: 5:20 AM
 */
public class Exercise25 {
    private static int numOfInts = 4000000;
    private static int numOfUbuffInts = 200000;

    private abstract static class Tester {
        private String name;
        private static int testRepeats = 50;
        private List<Long> durations;

        public static int[] buffSizes = {1, 32, 64, 128, 256, 512, 1024, 2048, 9000, 10240, 102400,  3, 44, 666 };
        
        public Tester(String name) {
            this.name = name;
            durations = new LinkedList<Long>();
        }
        
        public Tester(String name, int repeatCount) {
            this.name = name;
            this.testRepeats = repeatCount;
            durations = new LinkedList<Long>();
        }
        
        public void runTest() {
            System.out.print(name + ": ");
            try {
                before();
                for(int i = 0; i < testRepeats; i++) {
                    long start = System.nanoTime();
                    test();
                    long duration = System.nanoTime() - start;
                    durations.add(duration);
                }
                double avg = getAverageDuration();
                System.out.println(avg);
                after();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }

        protected double  getAverageDuration() {
            long sum = 0;
            for(Long duration: durations)
                sum += duration;
            return sum/(testRepeats * 1.0e9);
        }
        
        protected void deleteFile(String fileName) {
            File f = new File(fileName);

            // Make sure the file or directory exists and isn't write protected
            if (!f.exists())
                throw new IllegalArgumentException(
                        "Delete: no such file or directory: " + fileName);

            if (!f.canWrite())
                throw new IllegalArgumentException("Delete: write protected: "
                        + fileName);

            // If it is a directory, make sure it is empty
            if (f.isDirectory()) {
                String[] files = f.list();
                if (files.length > 0)
                    throw new IllegalArgumentException(
                            "Delete: directory not empty: " + fileName);
            }

            // Attempt to delete it
            boolean success = f.delete();

            if (!success)
                throw new IllegalArgumentException("Delete: deletion failed");
        }

        public abstract void test() throws IOException;
        public abstract void before() throws IOException;
        public abstract void after() throws IOException;
    }
    private static Tester[] tests = {
            new Tester("Buffer allocation", 2000) {
                public void before() {}
                public void test() throws IOException {
                    for(int i: buffSizes) {
                        ByteBuffer bb = ByteBuffer.allocate(i);
                    }
                }
                public void after() {}
            },

            new Tester("Buffer allocation(direct)", 2000) {
                public void before() {}
                public void test() throws IOException {
                    for(int i : buffSizes) {
                        ByteBuffer bb = ByteBuffer.allocateDirect(i);
                    }
                }
                public void after() {}
            },

            new Tester("GetChannel") {
                @Override
                public void test() throws IOException {
                    FileChannel fc =
                            new FileOutputStream("data.txt").getChannel();
                    fc.write(ByteBuffer.wrap("Some text ".getBytes()));
                    fc.close();
                    // Add to the end of the file:
                    fc =
                            new RandomAccessFile("data.txt", "rw").getChannel();
                    fc.position(fc.size()); // Move to the end
                    fc.write(ByteBuffer.wrap("Some more".getBytes()));
                    fc.close();
                    // Read the file:
                    fc = new FileInputStream("data.txt").getChannel();
                    ByteBuffer buff = ByteBuffer.allocate(1024);
                    fc.read(buff);
                    buff.flip();
                    while(buff.hasRemaining()) {
                        char temp = (char)buff.get();
                    }
                }

                @Override
                public void before() throws IOException {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void after() throws IOException {
                    deleteFile("data.txt");
                }
            },

            new Tester("GetChannel(direct)") {
                @Override
                public void test() throws IOException {
                    FileChannel fc =
                            new FileOutputStream("data.txt").getChannel();
                    fc.write(ByteBuffer.wrap("Some text ".getBytes()));
                    fc.close();
                    // Add to the end of the file:
                    fc =
                            new RandomAccessFile("data.txt", "rw").getChannel();
                    fc.position(fc.size()); // Move to the end
                    fc.write(ByteBuffer.wrap("Some more".getBytes()));
                    fc.close();
                    // Read the file:
                    fc = new FileInputStream("data.txt").getChannel();
                    ByteBuffer buff = ByteBuffer.allocateDirect(1024);
                    fc.read(buff);
                    buff.flip();
                    while(buff.hasRemaining()) {
                        char temp = (char)buff.get();
                    }

                }

                @Override
                public void before() throws IOException {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void after() throws IOException {
                    deleteFile("data.txt");
                }
            },

            new Tester("Channel copy", 10) {
                @Override
                public void test() throws IOException {

                    FileChannel
                            in = new FileInputStream("source.tmp").getChannel(),
                            out = new FileOutputStream("dest.tmp").getChannel();
                    ByteBuffer buffer = ByteBuffer.allocate(10240);
                    while(in.read(buffer) != -1) {
                        buffer.flip(); // Prepare for writing
                        out.write(buffer);
                        buffer.clear();
                    }
                    in.close();
                    out.close();

                }

                @Override
                public void before() throws IOException {
                    PrintWriter out = new PrintWriter("source.tmp");
                    for(int i = 0; i < numOfInts; i++)
                            out.write(i);
                    out.close();
                }
                
                @Override
                public void after() throws IOException {
                    deleteFile("source.tmp");
                    deleteFile("dest.tmp");
                }
            },

            new Tester("Channel copy(direct)", 10) {
                @Override
                public void test() throws IOException {

                    FileChannel
                            in = new FileInputStream("source.tmp").getChannel(),
                            out = new FileOutputStream("dest.tmp").getChannel();
                    ByteBuffer buffer = ByteBuffer.allocateDirect(10240);
                    while(in.read(buffer) != -1) {
                        buffer.flip(); // Prepare for writing
                        out.write(buffer);
                        buffer.clear();
                    }
                    in.close();
                    out.close();

                }

                @Override
                public void before() throws IOException {
                    PrintWriter out = new PrintWriter("source.tmp");
                    for(int i = 0; i < numOfInts; i++)
                        out.write(i);
                    out.close();
                }

                @Override
                public void after() throws IOException {
                    deleteFile("source.tmp");
                    deleteFile("dest.tmp");
                }
            },

            new Tester("BufferToText") {

                @Override
                public void test() throws IOException {
                    FileChannel fc =
                            new FileOutputStream("data2.txt").getChannel();
                    fc.write(ByteBuffer.wrap("Some text".getBytes()));
                    fc.close();
                    fc = new FileInputStream("data2.txt").getChannel();
                    ByteBuffer buff = ByteBuffer.allocate(1024);
                    fc.read(buff);
                    buff.flip();
                    // Doesn't work:
                    buff.asCharBuffer();
                    // Decode using this system's default Charset:
                    buff.rewind();
                    String encoding = System.getProperty("file.encoding");
                    Charset.forName(encoding).decode(buff);
                    // Or, we could encode with something that will print:
                    fc = new FileOutputStream("data2.txt").getChannel();
                    fc.write(ByteBuffer.wrap(
                            "Some text".getBytes("UTF-16BE")));
                    fc.close();
                    // Now try reading again:
                    fc = new FileInputStream("data2.txt").getChannel();
                    buff.clear();
                    fc.read(buff);
                    buff.flip();
                    buff.asCharBuffer();
                    // Use a CharBuffer to write through:
                    fc = new FileOutputStream("data2.txt").getChannel();
                    buff = ByteBuffer.allocate(24); // More than needed
                    buff.asCharBuffer().put("Some text");
                    fc.write(buff);
                    fc.close();
                    // Read and display:
                    fc = new FileInputStream("data2.txt").getChannel();
                    buff.clear();
                    fc.read(buff);
                    buff.flip();
                    buff.asCharBuffer();
                }

                @Override
                public void before() throws IOException {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void after() throws IOException {
                    deleteFile("data2.txt");
                }
            },

            new Tester("BufferToText(direct)") {

                @Override
                public void test() throws IOException {
                    FileChannel fc =
                            new FileOutputStream("data2.txt").getChannel();
                    fc.write(ByteBuffer.wrap("Some text".getBytes()));
                    fc.close();
                    fc = new FileInputStream("data2.txt").getChannel();
                    ByteBuffer buff = ByteBuffer.allocateDirect(1024);
                    fc.read(buff);
                    buff.flip();
                    // Doesn't work:
                    buff.asCharBuffer();
                    // Decode using this system's default Charset:
                    buff.rewind();
                    String encoding = System.getProperty("file.encoding");
                    Charset.forName(encoding).decode(buff);
                    // Or, we could encode with something that will print:
                    fc = new FileOutputStream("data2.txt").getChannel();
                    fc.write(ByteBuffer.wrap(
                            "Some text".getBytes("UTF-16BE")));
                    fc.close();
                    // Now try reading again:
                    fc = new FileInputStream("data2.txt").getChannel();
                    buff.clear();
                    fc.read(buff);
                    buff.flip();
                    buff.asCharBuffer();
                    // Use a CharBuffer to write through:
                    fc = new FileOutputStream("data2.txt").getChannel();
                    buff = ByteBuffer.allocateDirect(24); // More than needed
                    buff.asCharBuffer().put("Some text");
                    fc.write(buff);
                    fc.close();
                    // Read and display:
                    fc = new FileInputStream("data2.txt").getChannel();
                    buff.clear();
                    fc.read(buff);
                    buff.flip();
                    buff.asCharBuffer();
                }

                @Override
                public void before() throws IOException {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void after() throws IOException {
                    deleteFile("data2.txt");
                }
            },
            
            new Tester("GetData") {
                @Override
                public void test() throws IOException {
                    ByteBuffer bb = ByteBuffer.allocate(1024);
                    // Allocation automatically zeroes the ByteBuffer:
                    int i = 0;
                    while(i++ < bb.limit())
                        if(bb.get() != 0)
                            print("nonzero");
                    //print("i = " + i);
                    bb.rewind();
                    // Store and read a char array:
                    bb.asCharBuffer().put("Howdy!");
                    char c;
                    while((c = bb.getChar()) != 0);
                    //    printnb(c + " ");
                    //print();
                    bb.rewind();
                    // Store and read a short:
                    bb.asShortBuffer().put((short)471142);
                    bb.getShort();
                    bb.rewind();
                    // Store and read an int:
                    bb.asIntBuffer().put(99471142);
                    bb.getInt();
                    bb.rewind();
                    // Store and read a long:
                    bb.asLongBuffer().put(99471142);
                    bb.getLong();
                    bb.rewind();
                    // Store and read a float:
                    bb.asFloatBuffer().put(99471142);
                    bb.getFloat();
                    bb.rewind();
                    // Store and read a double:
                    bb.asDoubleBuffer().put(99471142);
                    bb.getDouble();
                    bb.rewind();
                }

                @Override
                public void before() throws IOException {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void after() throws IOException {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            },
            
            new Tester("GetData(direct)") {
                @Override
                public void test() throws IOException {
                    ByteBuffer bb = ByteBuffer.allocateDirect(1024);
                    // Allocation automatically zeroes the ByteBuffer:
                    int i = 0;
                    while(i++ < bb.limit())
                        if(bb.get() != 0)
                            print("nonzero");
                    //print("i = " + i);
                    bb.rewind();
                    // Store and read a char array:
                    bb.asCharBuffer().put("Howdy!");
                    char c;
                    while((c = bb.getChar()) != 0);
                        //    printnb(c + " ");
                        //print();
                        bb.rewind();
                    // Store and read a short:
                    bb.asShortBuffer().put((short)471142);
                    bb.getShort();
                    bb.rewind();
                    // Store and read an int:
                    bb.asIntBuffer().put(99471142);
                    bb.getInt();
                    bb.rewind();
                    // Store and read a long:
                    bb.asLongBuffer().put(99471142);
                    bb.getLong();
                    bb.rewind();
                    // Store and read a float:
                    bb.asFloatBuffer().put(99471142);
                    bb.getFloat();
                    bb.rewind();
                    // Store and read a double:
                    bb.asDoubleBuffer().put(99471142);
                    bb.getDouble();
                    bb.rewind();
                }

                @Override
                public void before() throws IOException {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void after() throws IOException {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            },

            new Tester("IntBufferDemo") {
                @Override
                public void test() throws IOException {
                    ByteBuffer bb = ByteBuffer.allocate(1024);
                    IntBuffer ib = bb.asIntBuffer();
                    // Store an array of int:
                    ib.put(new int[]{ 11, 42, 47, 99, 143, 811, 1016 });
                    // Absolute location read and write:
                    ib.get(3);
                    ib.put(3, 1811);
                    // Setting a new limit before rewinding the buffer.
                    ib.flip();
                    while(ib.hasRemaining()) {
                        int i = ib.get();
                        //System.out.println(i);
                    }
                }

                @Override
                public void before() throws IOException {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void after() throws IOException {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            },

            new Tester("IntBufferDemo(direct)") {
                @Override
                public void test() throws IOException {
                    ByteBuffer bb = ByteBuffer.allocateDirect(1024);
                    IntBuffer ib = bb.asIntBuffer();
                    // Store an array of int:
                    ib.put(new int[]{ 11, 42, 47, 99, 143, 811, 1016 });
                    // Absolute location read and write:
                    ib.get(3);
                    ib.put(3, 1811);
                    // Setting a new limit before rewinding the buffer.
                    ib.flip();
                    while(ib.hasRemaining()) {
                        int i = ib.get();
                        //System.out.println(i);
                    }
                }

                @Override
                public void before() throws IOException {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void after() throws IOException {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            },

            new Tester("UsingBuffers") {
                @Override
                public void test() throws IOException {
                    char[] data = TextFile.read("./io/exercises/Exercise25.java").toCharArray();
                    if ((data.length % 2) != 0)
                        data = Arrays.copyOfRange(data,0, data.length - 1);

                    ByteBuffer bb = ByteBuffer.allocate(data.length * 2);
                    CharBuffer cb = bb.asCharBuffer();
                    cb.put(data);
                    cb.rewind();
                    symmetricScramble(cb);
                    cb.rewind();
                    symmetricScramble(cb);
                    cb.rewind();
                }

                private void symmetricScramble(CharBuffer buffer){
                    while(buffer.hasRemaining()) {
                        buffer.mark();
                        char c1 = buffer.get();
                        char c2 = buffer.get();
                        buffer.reset();
                        buffer.put(c2).put(c1);
                    }
                }

                @Override
                public void before() throws IOException {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void after() throws IOException {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            },

            new Tester("UsingBuffers(direct)") {
                @Override
                public void test() throws IOException {
                    char[] data = TextFile.read("./io/exercises/Exercise25.java").toCharArray();

                    if ((data.length % 2) != 0)
                        data = Arrays.copyOfRange(data,0, data.length - 1);

                    ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 2);
                    CharBuffer cb = bb.asCharBuffer();
                    cb.put(data);
                    cb.rewind();
                    symmetricScramble(cb);
                    cb.rewind();
                    symmetricScramble(cb);
                    cb.rewind();
                }

                private void symmetricScramble(CharBuffer buffer){
                    while(buffer.hasRemaining()) {
                        buffer.mark();
                        char c1 = buffer.get();
                        char c2 = buffer.get();
                        buffer.reset();
                        buffer.put(c2).put(c1);
                    }
                }

                @Override
                public void before() throws IOException {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void after() throws IOException {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            }
    };

    public static void main(String[] args) {
        for(Tester test : tests)
            test.runTest();
    }

    public static long allocationTime(int i) {
        long start = System.nanoTime();
        ByteBuffer bb = ByteBuffer.allocate(i);
        return System.nanoTime() - start;
    }

    public static long directAllocationTime(int i) {
        long start = System.nanoTime();
        ByteBuffer bb = ByteBuffer.allocateDirect(i);
        return System.nanoTime() - start;
    }
}
