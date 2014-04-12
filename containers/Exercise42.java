package containers;

import net.mindview.util.Generator;
import net.mindview.util.RandomGenerator;

import java.util.*;

import static containers.Exercise40.printArray;
import static net.mindview.util.Print.print;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2/2/12
 * Time: 11:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Exercise42 {
    public static class Comp2 implements Comparator<StringFirst40> {
        public int compare(StringFirst40 a, StringFirst40 b) {
            return a.second.compareToIgnoreCase(b.second);
        }
    }
    public static void main(String[] args) {
        Generator<String> gen = new RandomGenerator.String(20);
        int count = 1000;
        StringFirst40[] firstArray = new StringFirst40[count];
        List<StringFirst40> firstList = new ArrayList<StringFirst40>(count);

        for(int i = 0; i < count; i++) {
            firstArray[i] = new StringFirst40(gen.next(), gen.next());

        }
        firstList.addAll(Arrays.asList(firstArray));

        printArray(firstArray);
        print(firstList);

        Arrays.sort(firstArray);
        Collections.sort(firstList);

        printArray(firstArray);
        print(firstList);

        Arrays.sort(firstArray, new Comp2());
        Collections.sort(firstList, new Comp2());

        printArray(firstArray);
        print(firstList);

        Random rand = new Random();
        int index = -1;

        do{
            index = rand.nextInt(count);
        } while( index <= 0);

        StringFirst40 randItem = firstArray[index];

        int found1 = Arrays.binarySearch(firstArray, randItem, new Comp2());
        int found2 = Collections.binarySearch(firstList, randItem, new Comp2());
        print(firstArray[found1].equals(firstArray[index]));
        print(firstList.get(found2).equals(firstList.get(index)));

    }
}
