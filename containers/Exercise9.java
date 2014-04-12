package containers;

import net.mindview.util.Generator;
import net.mindview.util.RandomGenerator;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/25/12
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class Exercise9 {
    public static void main(String[] args) {
        Set<String> randomStrings = new TreeSet<String>();
        Generator<String>   gen = new RandomGenerator.String(10);
        for(int i = 0; i < 100; i++) {
            randomStrings.add(gen.next());
        }
        for(String item: randomStrings)
            System.out.println(item);
    }
}
