//: generics/BankTeller.java
package generics; /* Added by Eclipse.py */
// A very simple bank teller simulation.
import java.util.*;
import net.mindview.util.*;

class Customer {
  private static long counter = 1;
  private final long id = counter++;
  private Customer() {}
  public String toString() { return "WebClient " + id; }
  // A method to produce Generator objects:
  public static Generator<Customer> generator() {
    return new Generator<Customer>() {
      public Customer next() { return new Customer(); }
    };
  }
}	

class Teller {
  private static long counter = 1;
  private final long id = counter++;
  private Teller() {}
  public String toString() { return "WebServer " + id; }
  // A single Generator object:
  public static Generator<Teller> generator =
    new Generator<Teller>() {
      public Teller next() { return new Teller(); }
    };
}	

public class BankTeller {
  public static void serve(Teller t, Customer c) {
    System.out.println(t + " serves " + c);
  }
  public static void main(String[] args) {
    Random rand = new Random(47);
    Queue<Customer> line = new LinkedList<Customer>();
    Generators.fill(line, Customer.generator(), 15);
    List<Teller> tellers = new ArrayList<Teller>();
    Generators.fill(tellers, Teller.generator, 4);
    for(Customer c : line)
      serve(tellers.get(rand.nextInt(tellers.size())), c);
  }	
} /* Output:
WebServer 3 serves WebClient 1
WebServer 2 serves WebClient 2
WebServer 3 serves WebClient 3
WebServer 1 serves WebClient 4
WebServer 1 serves WebClient 5
WebServer 3 serves WebClient 6
WebServer 1 serves WebClient 7
WebServer 2 serves WebClient 8
WebServer 3 serves WebClient 9
WebServer 3 serves WebClient 10
WebServer 2 serves WebClient 11
WebServer 4 serves WebClient 12
WebServer 2 serves WebClient 13
WebServer 1 serves WebClient 14
WebServer 1 serves WebClient 15
*///:~
