package containers;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/29/12
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
// containers/SimpleHashMap25.java
// TIJ4 Chapter Containers, Exercise 25, page 851
/* Instead of using a ListIterator for each bucket, modify MapEntry so that
* it is a self-contained singly linked list (each MapEntry should have a
* forward link to the next MapEntry). Modify the rest of the code in
* SimpleHashMap.java so that this new approach works correctly.
*/

/***
* My solution to one of the exercises in
* Thinking in Java 4th Edition (by Bruce Eckel);
* it compiles and runs correctly using JDK 1.6.0
* @author Greg Gordon
* @author www.greggordon.org
* December, 2007
***/

import java.util.*;
import net.mindview.util.*;

public class SimpleHashMap25<K,V> extends AbstractMap<K,V> {
	class MapEntry25<K,V> implements Map.Entry<K,V> {
		private K key;
		private V value;
		private MapEntry25<K,V> nextEntry = null;
		public MapEntry25(K key, V value) {
			this.key = key;
			this.value = value;
		}
		public K getKey() { return key; }
		public V getValue() { return value; }
		public V setValue(V v) {
			V result = value;
			value = v;
			return result;
		}
		public MapEntry25<K,V> getNextEntry() {
			return this.nextEntry;
		}
		public void setNextEntry(MapEntry25<K,V> nextEntry) {
			this.nextEntry = nextEntry;
		}
		public int hashCode() {
			return (key == null ? 0 : key.hashCode()) ^
				(value == null ? 0 : value.hashCode());
		}
		public boolean equals(Object o) {
			if(!(o instanceof MapEntry25)) return false;
			MapEntry25 me = (MapEntry25) o;
			return
				(key == null ?
				me.getKey() == null : key.equals(me.getKey())) &&
				(value == null ?
				me.getValue() == null : value.equals(me.getValue()));
		}
		public String toString() { return key + "=" + value; }
	}
	// Choose a prime number for the hash table
	// size, to achieve a uniform distribution:
	static final int SIZE = 997;
	// You can't have a physical array of generics,
	// but you can upcast to one:
	@SuppressWarnings("unchecked")
	LinkedList<MapEntry25<K,V>>[] buckets =
		new LinkedList[SIZE];
	public V put(K key, V value) {
		V oldValue = null;
		MapEntry25<K,V> pair = new MapEntry25<K,V>(key, value);
		int index = Math.abs(key.hashCode()) % SIZE;
		if(buckets[index] == null) {
			buckets[index] = new LinkedList<MapEntry25<K,V>>();
			LinkedList<MapEntry25<K,V>> bucket = buckets[index];
			bucket.add(pair);
		}
		LinkedList<MapEntry25<K,V>> bucket = buckets[index];
		if(buckets[index].size() > 0) {
			for(MapEntry25<K,V> entry = bucket.get(0); entry != null;
			entry = entry.getNextEntry()) {
				if(entry.getKey().equals(key)) {
				oldValue = entry.getValue();
				entry.setValue(value);
				return oldValue;
				}
				}
			bucket.add(pair);
			int i = bucket.indexOf(pair);
			if(i > 0) bucket.get(i - 1).setNextEntry(pair);
			return pair.getValue();
		}
		return oldValue;
	}
	public V get(Object key) {
		int index = Math.abs(key.hashCode()) % SIZE;
		if(buckets[index] == null) return null;
		for(MapEntry25<K,V> iPair : buckets[index])
			if(iPair.getKey().equals(key))
				return iPair.getValue();
		return null;
	}
	public Set<Map.Entry<K,V>> entrySet() {
		Set<Map.Entry<K,V>> set = new HashSet<Map.Entry<K,V>>();
		for(LinkedList<MapEntry25<K,V>> bucket : buckets) {
			if(bucket == null) continue;
			for(MapEntry25<K,V> mpair : bucket)
				set.add(mpair);
		}
		return set;
	}
	public static void main(String[] args) {
		SimpleHashMap25<String,String> m =
			new SimpleHashMap25<String,String>();
		m.putAll(Countries.capitals(5));
		System.out.println(m);
		System.out.println(m.put("BENIN","New York?"));
		System.out.println(m.put("BENIN","Porto-Novo"));
		System.out.println(m.get("BENIN"));
		System.out.println(m.entrySet());
	}
}
