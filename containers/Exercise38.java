package containers;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2/2/12
 * Time: 11:24 AM
 * To change this template use File | Settings | File Templates.
 */
import java.util.*;
import net.mindview.util.*;

import static net.mindview.util.Print.print;


class HashMaps38 {
    static List<Test<Map<Integer,String>>> tests =
            new ArrayList<Test<Map<Integer,String>>>();
    static CountingGenerator.String cgs = new CountingGenerator.String(5);
    static {
        tests.add(new Test<Map<Integer,String>>("put") {
            int test(Map<Integer,String> map, TestParam tp) {
                int loops = tp.loops;
                int size = tp.size;
                for(int i = 0; i < loops; i++) {
                    map.clear();
                    for(int j = 0; j < size; j++)
                        map.put(j, cgs.next());
                }
                return loops * size;
            }
        });
        tests.add(new Test<Map<Integer,String>>("get") {
            int test(Map<Integer,String> map, TestParam tp) {
                int loops = tp.loops;
                int span = tp.size * 2;
                for(int i = 0; i < loops; i++)
                    for(int j = 0; j < span; j++)
                        map.get(j);
                return loops * span;
            }
        });
        tests.add(new Test<Map<Integer,String>>("iterate") {
            int test(Map<Integer,String> map, TestParam tp) {
                int loops = tp.loops * 10;
                for(int i = 0; i < loops; i++) {
                    Iterator it = map.entrySet().iterator();
                    while(it.hasNext()) it.next();
                }
                return loops * map.size();
            }
        });
    }
    public static void main(String[] args) {
        HashMap<Integer,String> map1 = new HashMap<Integer,String>();
        print("map1: " + map1);
        map1.putAll(new CountingMapData(55));
        print("map1: " + map1);

        HashMap<Integer,String> map2 = new HashMap<Integer,String>(64);
        print("map2: " + map2);
        map2.putAll(map1);
        print("map2: " + map2);

        HashMap<Integer,String> map3 = new HashMap<Integer,String>(1024);
        print("map3: " + map3);
        map3.putAll(map1);
        print("map3: " + map3);

        HashMap<Integer,String> map4 = new HashMap<Integer,String>(1024, 0.10f);
        print("map4: " + map4);
        map4.putAll(map1);
        print("map4: " + map4);

        HashMap<Integer,String> map5 = new HashMap<Integer,String>(1024, 0.25f);
        print("map5: " + map5);
        map5.putAll(map1);
        print("map5: " + map5);

        HashMap<Integer,String> map6 = new HashMap<Integer,String>(1024, 0.50f);
        print("map6: " + map6);
        map6.putAll(map1);
        print("map6: " + map6);

        HashMap<Integer,String> map7 = new HashMap<Integer,String>(1024, 0.99f);
        print("map7: " + map7);
        map7.putAll(map1);
        print("map7: " + map7);

        Tester.defaultParams = TestParam.array(10, 1000, 100, 1000, 1000, 1000, 10000, 10000);
        print("Comparative time tests:");
        Tester.run(map1, tests);
        Tester.run(map2, tests);
        Tester.run(map3, tests);
        Tester.run(map4, tests);
        Tester.run(map5, tests);
        Tester.run(map6, tests);
        Tester.run(map7, tests);
    }
}

public class Exercise38 {
    public static void main(String[] args) {
        HashMaps38.main(null);
    }
}
