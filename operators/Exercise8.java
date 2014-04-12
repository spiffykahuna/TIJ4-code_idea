//: operators/Literals.java
package operators; /* Added by Eclipse.py */
import static net.mindview.util.Print.*;

public class Exercise8 {
  public static void main(String[] args) {
    int i1 = 0x2f; // Hexadecimal (lowercase)
    print("i1: " + Integer.toBinaryString(i1));
    int i2 = 0X2F; // Hexadecimal (uppercase)
    print("i2: " + Integer.toBinaryString(i2));
    int i3 = 0177; // Octal (leading zero)
    print("i3: " + Integer.toBinaryString(i3));
    char c = 0xffff; // max char hex value
    print("c: " + Integer.toBinaryString(c));
    byte b = 0x7f; // max byte hex value
    print("b: " + Integer.toBinaryString(b));
    short s = 0x7fff; // max short hex value
    print("s: " + Integer.toBinaryString(s));
    long n1 = 200L; // long suffix
    long n2 = 200l; // long suffix (but can be confusing)
    long n3 = 200;
    long n4 = 0177;
    long n5 = 0xFFFFFFFF;
    print("n4: " + Long.toBinaryString(n4));
    print("n5: " + Long.toBinaryString(n5));
    print("minFloat: " + Float.MIN_VALUE);
    print("maxFloat: " + Float.MAX_VALUE);
    print("minDouble: " + Double.MIN_VALUE);
    print("maxDouble: " + String.format("%E", Double.MAX_VALUE));
    
    float f1 = 1;
    float f2 = 1F; // float suffix
    float f3 = 1f; // float suffix
    double d1 = 1d; // double suffix
    double d2 = 1D; // double suffix
    // (Hex and Octal also work with long)
  }
} /* Output:
i1: 101111
i2: 101111
i3: 1111111
c: 1111111111111111
b: 1111111
s: 111111111111111
*///:~
