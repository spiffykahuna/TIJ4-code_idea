package containers;

import net.mindview.util.Countries;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/27/12
 * Time: 6:24 PM
 * To change this template use File | Settings | File Templates.
 */
class SimpleHashMapWithCollisions<K, V> extends SimpleHashMap<K, V> {
    public V put(K key, V value) {
        V oldValue = null;
        int index = Math.abs(key.hashCode()) % SIZE;
        if(buckets[index] == null)
          buckets[index] = new LinkedList<MapEntry<K,V>>();

        LinkedList<MapEntry<K,V>> bucket = buckets[index];
        MapEntry<K,V> pair = new MapEntry<K,V>(key, value);
        boolean found = false;
        ListIterator<MapEntry<K,V>> it = bucket.listIterator();
        while(it.hasNext()) {
          MapEntry<K,V> iPair = it.next();
          if(iPair.getKey().equals(key)) {
            System.out.println("Collision: new " +
                pair + " for old " + iPair);
            oldValue = iPair.getValue();
            it.set(pair); // Replace old with new
            found = true;
            break;
          }
        }
        if(!found)
          buckets[index].add(pair);
        return oldValue;
    }

    public V get(Object key) {
        int index = Math.abs(key.hashCode()) % SIZE;
        if(buckets[index] == null) return null;
        for(MapEntry<K,V> iPair : buckets[index])
          if(iPair.getKey().equals(key))
            return iPair.getValue();
        return null;
    }
}

public class Exercise20 {
    public static void main(String[] args) {
        SimpleHashMapWithCollisions<String,String> m =
			new SimpleHashMapWithCollisions<String,String>();
		m.putAll(Countries.capitals(10));
		System.out.println(m);
		m.put("EGYPT","Berlin?");
		m.put("EGYPT","Cairo");
		System.out.println(m);
		m.putAll(Countries.capitals(10));
    }
}
