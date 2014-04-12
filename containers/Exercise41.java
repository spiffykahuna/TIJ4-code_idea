package containers;

import net.mindview.util.Generator;
import net.mindview.util.RandomGenerator;

import java.util.*;

import static net.mindview.util.Print.print;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2/2/12
 * Time: 11:03 PM
 * To change this template use File | Settings | File Templates.
 */
class StringKey41 extends StringFirst40 {

    public StringKey41(String first, String second) {
        super(first, second);
    }
}
public class Exercise41 {
    public static void main(String[] args) {
        Generator<String> gen = new RandomGenerator.String(20);
        Set<StringKey41> set = new HashSet<StringKey41>();
        final  int count = 1000;
        for(int i = 0; i < count; i++) {
            set.add(new StringKey41(gen.next(), gen.next()));
        }

        print(set);
        Map<StringKey41, String> map = new HashMap<StringKey41, String>();
        for(int i = 0; i < count; i++) {
            map.put(new StringKey41(gen.next(), gen.next()), gen.next());
        }
        print(map);

    }
}
