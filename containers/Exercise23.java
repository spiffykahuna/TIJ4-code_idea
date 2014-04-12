package containers;

import net.mindview.util.Countries;

import java.io.IOException;
import java.util.*;

import static net.mindview.util.Print.print;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/27/12
 * Time: 6:57 PM
 * To change this template use File | Settings | File Templates.
 */

public class Exercise23 {
    public static void main(String[] args) {
        SimpleHashMapComplete<String,String> m =
			new SimpleHashMapComplete<String,String>();
		m.putAll(Countries.capitals(5));
        // не забыть про  return entrySet().size();
        print("Test size(): " + (m.size() == 5));
        SimpleHashMapComplete<Integer, String> map2 = new SimpleHashMapComplete<Integer, String>();
        map2.put(2, null);
        map2.put(4, "hehe");
        print("Test containsKey(): " + map2.containsKey(2));
        print("Test containsValue(): " + map2.containsValue("hehe"));
        map2.put(4, "haha");
        print("Test containsValue(): " + map2.containsValue("hehe"));
        map2.put(10, "ten");
        //map.put(2, "dva");
        print("Before: " + map2);
        map2.remove(2);
        print("After: " + map2);



        HashMap<Integer, String>  hashMap = new HashMap<Integer, String>();
        hashMap.put(2, null);
        print("HashMap test: " + hashMap);
        hashMap.remove(2);
        print("HashMap check: " + hashMap);

        hashMap.put(null, null);
        map2.put(null, null);
        print("null test " + hashMap.getClass().getSimpleName() + " : " + hashMap);
        print("null test " + map2.getClass().getSimpleName() + " : " + map2);

        SimpleHashMapComplete<String,String> map =
			new SimpleHashMapComplete<String,String>();

        map.putAll(Countries.capitals(3));
		print("map = " + map);
		print("map.entrySet(): " + map.entrySet());
		print("map.keySet(): " + map.keySet());
		print("map.values() = " + map.values());
		print("map.isEmpty(): " + map.isEmpty());
		print("map.containsKey(\"ALGERIA\"): " + map.containsKey("ALGERIA"));
		print("map.containsValue(\"Algiers\"): " + map.containsValue("Algiers"));
		print("map.get(\"ALGERIA\"): " + map.get("ALGERIA"));
		print("map.remove(\"ALGERIA\"): " + map.remove("ALGERIA"));
		print("After map.remove(\"ALGERIA\"), map.containsKey(\"ALGERIA\"): " +
			map.containsKey("ALGERIA"));
		print(" and map.get(\"ALGERIA\"): " + map.get("ALGERIA"));
		print(" and map: = " + map);
		map.clear();
		print("After map.clear(), map = " + map);
		print(" and map.isEmpty(): " + map.isEmpty());
		map.putAll(Countries.capitals(3));
		print("After map.putAll(Countries.capitals(3)), map = " + map);
		SimpleHashMapComplete<String,String> map3 =
			new SimpleHashMapComplete<String,String>();
		map3.putAll(Countries.capitals(4));
		print("After map2.putAll(Countries.capitals(4)), map2 = " + map3);
		print(" and map.equals(map2): " + map.equals(map3));
		map3.remove("BOTSWANA");
		print("After map2.remove(\"BOTSWANT\"), map.equals(map2): " + map.equals(map3));
		map.entrySet().clear();
		print("After map.entrySet().clear, map = " + map);
		map.putAll(Countries.capitals(3));
		print("After map.putAll(Countries.capitals(3)), map = " + map);
		map.keySet().clear();
		print("After map.keySet().clear(), map = " + map);
    }
}
