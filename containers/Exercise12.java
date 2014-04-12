package containers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import static net.mindview.util.Print.print;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/27/12
 * Time: 8:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class Exercise12 {
    public static void main(String[] args) {
        AssociativeArray<String,String> map = new AssociativeArray<String,String>(6);
        map.put("sky", "blue");
        map.put("grass", "green");
        map.put("ocean", "dancing");
        map.put("tree", "tall");
        map.put("earth", "brown");
        map.put("sun", "warm");
        try {
          map.put("extra", "object"); // Past the end
        } catch(ArrayIndexOutOfBoundsException e) {
          print("Too many objects!");
        }
        print(map);
        print(map.get("ocean"));
        HashMap<String,String> hashMap =
			new HashMap<String,String>(6);
		hashMap.put("sky", "blue");
		hashMap.put("grass", "green");
		hashMap.put("ocean", "dancing");
		hashMap.put("tree", "tall");
		hashMap.put("earth", "brown");
		hashMap.put("sun", "warm");
		try {
			hashMap.put("extra", "object"); // Past the end
		} catch(ArrayIndexOutOfBoundsException e) {
			print("Too many objects");
		}
		print(hashMap);
		print(hashMap.get("ocean"));
		TreeMap<String,String> treeMap =
			new TreeMap<String,String>();
		treeMap.put("sky", "blue");
		treeMap.put("grass", "green");
		treeMap.put("ocean", "dancing");
		treeMap.put("tree", "tall");
		treeMap.put("earth", "brown");
		treeMap.put("sun", "warm");
		try {
			treeMap.put("extra", "object"); // Past the end
		} catch(ArrayIndexOutOfBoundsException e) {
			print("Too many objects");
		}
		print(treeMap);
		print(treeMap.get("ocean"));

		LinkedHashMap<String,String> linkedHashMap =
			new LinkedHashMap<String,String>(6);
		linkedHashMap.put("sky", "blue");
		linkedHashMap.put("grass", "green");
		linkedHashMap.put("ocean", "dancing");
		linkedHashMap.put("tree", "tall");
		linkedHashMap.put("earth", "brown");
		linkedHashMap.put("sun", "warm");
		try {
			linkedHashMap.put("extra", "object"); // Too far
		} catch(ArrayIndexOutOfBoundsException e) {
			print("Too many objects");
		}
		print(linkedHashMap);
		print(linkedHashMap.get("ocean"));

    }

}
