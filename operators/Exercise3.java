//: operators/PassObject.java
package operators; /* Added by Eclipse.py */
// Passing objects to methods may not be
// what you're used to.
import static net.mindview.util.Print.*;

class Nomerok {
  float nomerok;
}

public class Exercise3 {
  static void f(Nomerok y) {
    y.nomerok = 666.666f;
  }
  public static void main(String[] args) {
    Nomerok x = new Nomerok();
    x.nomerok = 1234.5f;
    print("1: x.nomerok: " + x.nomerok);
    f(x);
    print("2: x.nomerok: " + x.nomerok);
  }
} /* Output:
1: x.c: a
2: x.c: z
*///:~
