//: operators/EqualsMethod2.java
package operators; /* Added by Eclipse.py */
// Default equals() does not compare contents.
import java.util.Random;

class Coin {
	Boolean state;	
}

public class Exercise7 {
  public static void main(String[] args) {
    Random rand = new Random();
    
    Coin myCoin = new Coin();
    
    for(int i = 0; i<rand.nextInt(1000); i++) {
    	myCoin.state = rand.nextBoolean();
    	System.out.println("Coin state is: " + myCoin.state);
    }
  }
} /* Output:
false
*///:~
