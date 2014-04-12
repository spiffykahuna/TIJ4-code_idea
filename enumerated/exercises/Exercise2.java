//: enumerated/cartoons/EnumImplementation.java
// An enum can implement an interface
package enumerated.exercises;

import net.mindview.util.Generator;

import java.util.Random;

enum CartoonCharacter {
  SLAPPY, SPANKY, PUNCHY, SILLY, BOUNCY, NUTTY, BOB;

  private static Random rand = new Random(47);

  public static CartoonCharacter next() {
    return values()[rand.nextInt(values().length)];
  }
}

public class Exercise2 {
  public static void main(String[] args) {
    for(int i = 0; i < 10; i++)
        System.out.print(CartoonCharacter.next() + ", ");

  }
} /* Output:
BOB, PUNCHY, BOB, SPANKY, NUTTY, PUNCHY, SLAPPY, NUTTY, NUTTY, SLAPPY,
*///:~
