//: operators/EqualsMethod2.java
package operators; /* Added by Eclipse.py */
// Default equals() does not compare contents.
class Dog {
	String name;
	String says;
}

public class Exercise5and6 {
  public static void main(String[] args) {
    Dog spot = new Dog();
    spot.name = "Spot";
    spot.says = "Ryff!";
    Dog scruffy = new Dog();
    scruffy.name = "Scruffy";
    scruffy.says = "Wurf!";
    
    System.out.println(spot.name + " says: " + spot.says);
    System.out.println(scruffy.name + " says: " + scruffy.says);
    
    Dog bobik = new Dog();
    bobik.name = "Bobik";
    bobik.says = "GAV!";
    
    spot = bobik;
    System.out.println(spot == bobik);
    System.out.println(spot.equals(bobik));
    System.out.println(scruffy == bobik);
    System.out.println(scruffy.equals(bobik));
    
   // System.out.println(v1.equals(v2));
  }
} /* Output:
false
*///:~
