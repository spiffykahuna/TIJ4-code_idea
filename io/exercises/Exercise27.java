package io.exercises;

import java.io.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2013-12-15
 * Time: 9:20 PM
 */
public class Exercise27 {

    public static final String FILE_NAME = "serial.out";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Inner inner = new Inner(42);
        Outer outer = new Outer(inner, 43);

        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(FILE_NAME));
        out.writeObject(outer);
        out.close();

        ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(FILE_NAME));
        Outer outerClone = (Outer) in.readObject();
        in.close();

        assertEquals(outer, outerClone);
        assertEquals(outer.getInner(), outerClone.getInner());
        assertEquals(outer.getData(), outerClone.getData());
        assertEquals(inner, outerClone.getInner());
        assertEquals(inner.getData(), outerClone.getInner().getData());

        System.out.println("Serialization was successful!");
    }
}

class Outer implements Serializable {

    private static final long serialVersionUID = -7410161566129274114L;
    private final Inner inner;
    private final int data;

    Outer(Inner inner, int data) {
        this.inner = inner;
        this.data = data;
    }

    public Inner getInner() {
        return inner;
    }

    public int getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Outer outer = (Outer) o;

        if (data != outer.data) return false;
        if (inner != null ? !inner.equals(outer.inner) : outer.inner != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = inner != null ? inner.hashCode() : 0;
        result = 31 * result + data;
        return result;
    }
}

class Inner implements Serializable {

    private static final long serialVersionUID = 1879127068237817667L;
    private final int data;

    Inner(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Inner inner = (Inner) o;

        if (data != inner.data) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return data;
    }
}
