package containers;

import net.mindview.util.RandomGenerator;

import java.util.PriorityQueue;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/26/12
 * Time: 11:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class Exercise11 extends PriorityQueue<Exercise11.IntItem> {
    static class  IntItem implements Comparable<IntItem> {
        private static  Random r = new Random(47);
        public int item;
        public IntItem() {item = r.nextInt(100);}

        public int compareTo(  IntItem i) {
            return (item < i.item ? -1 : ( item == i.item ? 0 : 1 ));
        }

        public String  toString() {
            return Integer.toString(item);
        }
    }

    public Exercise11(){}

    public static void main(String[] args) {
        Exercise11 ex = new Exercise11();
        for(int i = 0; i < 10; i++) {
            ex.add(new IntItem());
        }
        while(!ex.isEmpty())
            System.out.println(ex.poll());
    }
}
