package containers;

import net.mindview.util.Countries;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/27/12
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */

class SimpleHashedMapWithCollisions2<K, V> extends SimpleHashMapWithCollisions<K, V> {
    public V put(K key, V value) {
        V oldValue = null;
        int index = Math.abs(key.hashCode()) % SIZE;
        if(buckets[index] == null)
          buckets[index] = new LinkedList<MapEntry<K,V>>();

        LinkedList<MapEntry<K,V>> bucket = buckets[index];
        MapEntry<K,V> pair = new MapEntry<K,V>(key, value);
        boolean found = false;
        ListIterator<MapEntry<K,V>> it = bucket.listIterator();
        int probes = 0;
		while(it.hasNext()) {
			MapEntry<K,V> iPair = it.next();
			probes++;
			if(iPair.getKey().equals(key)) {
				oldValue = iPair.getValue();
				it.set(pair); // Replace old with new
				found = true;
				System.out.println("Collision at " +
					iPair + ": " + probes + " probe"
					+ ((probes == 1) ? "" : "s") +
					" needed");
				break;
			}
		}
        if(!found)
          buckets[index].add(pair);
        return oldValue;
    }

}

public class Exercise21 {
    public static void main(String[] args) {
        SimpleHashMapWithCollisions<String,String> m =
			new SimpleHashedMapWithCollisions2<String,String>();
		m.putAll(Countries.capitals(10));
		System.out.println(m);
		m.put("EGYPT","Berlin?");
		m.put("EGYPT","Cairo");
		System.out.println(m);
		m.putAll(Countries.capitals(10));
    }
}
