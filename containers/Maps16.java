package containers;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/27/12
 * Time: 4:21 PM
 * To change this template use File | Settings | File Templates.
 */
// containers/Maps16.java
// TIJ4 Chapter Containers, Exercise 16, page 847
// Apply the tests in Maps.java to SlowMap to verify that it works.
// Fix anything in SlowMap that doesn't work properly.
import net.mindview.util.*;
import static net.mindview.util.Print.*;
import java.util.*;


class SlowMap16<K,V> extends AbstractMap<K,V> {
	private List<K> keys = new ArrayList<K>();
	private List<V> values = new ArrayList<V>();
	private EntrySet entries = new EntrySet();
	public Set<Map.Entry<K,V>> entrySet() { return entries; }
	public V put(K key, V value) {
		V oldValue = get(key); // The old value or null
		if(!keys.contains(key)) {
			keys.add(key);
			values.add(value);
		} else
			values.set(keys.indexOf(key), value);
		return oldValue;
	}
	public V get(Object key) { // key is type Object, not K
		if(!keys.contains(key))
			return null;
		return values.get(keys.indexOf(key));
	}
	private class EntrySet extends AbstractSet<Map.Entry<K,V>> {
		public int size() { return keys.size(); }
		public Iterator<Map.Entry<K,V>> iterator() {
			return new Iterator<Map.Entry<K,V>>() {
				private int index = -1;
				public boolean hasNext() {
					return index < keys.size() - 1;
				}
				@SuppressWarnings("unchecked")
				public Map.Entry<K,V> next() {
					int i = ++index;
					return new MapEntry(
					keys.get(i), values.get(i));
				}
				public void remove() {
					keys.remove(index);
    					values.remove(index--);
				}
			};
		}
	}
}

public class Maps16 {
	public static void printKeys(Map<Integer,String> map) {
		printnb("Size = " + map.size() + ", ");
		printnb("Keys: ");
		print(map.keySet()); // Produce a set of the keys
	}
	public static void test(Map<Integer,String> map) {
		print(map.getClass().getSimpleName());
		map.putAll(new CountingMapData(25));
		// Map has 'Set' behavior for keys:
		map.putAll(new CountingMapData(25));
		printKeys(map);
		// Producing a collection of the values:
		printnb("Values: ");
		print(map.values());
		print(map);
		print("map.containsKey(11): " + map.containsKey(11));
		print("map.get(11): " + map.get(11));
		print("map.containsValue(\"F0\"): " + map.containsValue("F0"));
		Integer key = map.keySet().iterator().next();
		print("First key in map: " + key);
		map.remove(key);
		printKeys(map);
		map.clear();
		print("map.isEmpty(): " + map.isEmpty());
		map.putAll(new CountingMapData(25));
		// Operations on the Set change the Map:
		map.keySet().removeAll(map.keySet());
		print("map.isEmpty(): " + map.isEmpty());
	}
	public static void main(String[] args) {
		test(new HashMap<Integer,String>());
		print();
		test(new SlowMap<Integer,String>());
		print();
		test(new SlowMap16<Integer,String>());
	}
}
