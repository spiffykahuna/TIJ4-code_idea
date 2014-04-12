//: operators/Assignment.java
package operators; /* Added by Eclipse.py */
// Assignment with objects is a bit tricky.
import static net.mindview.util.Print.*;

class Tank2 {
  float level;
}	

public class Exercise2 {
  public static void main(String[] args) {
    Tank2 t1 = new Tank2();
    Tank2 t2 = new Tank2();
    t1.level = 9.4f;
    t2.level = 47.5f;
    print("1: t1.level: " + t1.level +
          ", t2.level: " + t2.level);
    t1.level = t2.level;
    print("2: t1.level: " + t1.level +
          ", t2.level: " + t2.level);
    t1.level = 27.31f;
    print("3: t1.level: " + t1.level +
          ", t2.level: " + t2.level);
    t2.level = 333.44f;
    print("4: t1.level: " + t1.level +
            ", t2.level: " + t2.level);
    t1.level = t2.level;
    print("5: t1.level: " + t1.level +
            ", t2.level: " + t2.level);
    t1.level = 13424.434f;
    print("6: t1.level: " + t1.level +
            ", t2.level: " + t2.level);
  }
} /* Output:
1: t1.level: 9, t2.level: 47
2: t1.level: 47, t2.level: 47
3: t1.level: 27, t2.level: 27
*///:~
