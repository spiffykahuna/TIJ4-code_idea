//: generics/InstantiateGenericType.java
package generics; /* Added by Eclipse.py */
import static net.mindview.util.Print.*;

class ClassAsFactory<T> {
  T x;
  public ClassAsFactory(Class<T> kind) {
    try {
      x = kind.newInstance();
    } catch(Exception e) {
      throw new RuntimeException(e);
    }
  }
}

class Employee2 {}

public class InstantiateGenericType {
  public static void main(String[] args) {
    ClassAsFactory<Employee2> fe =
      new ClassAsFactory<Employee2>(Employee2.class);
    print("ClassAsFactory<Employee2> succeeded");
    try {
      ClassAsFactory<Integer> fi =
        new ClassAsFactory<Integer>(Integer.class);
    } catch(Exception e) {
      print("ClassAsFactory<Integer> failed");
    }
  }
} /* Output:
ClassAsFactory<Employee2> succeeded
ClassAsFactory<Integer> failed
*///:~
