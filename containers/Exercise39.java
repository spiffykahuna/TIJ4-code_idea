package containers;

import containers.primefinder.PrimeIntegerGenerator;
import net.mindview.util.Countries;
import net.mindview.util.MapData;
import net.mindview.util.RandomGenerator;

import java.util.*;

import static net.mindview.util.Print.print;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2/2/12
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */
class SimpleHashMap39<K,V> extends AbstractMap<K,V> {
    // Choose a prime number for the hash table
    // size, to achieve a uniform distribution:
    static int CAPACITY = 997;
    static double loadFactor = 0.75;
    // You can't have a physical array of generics,
    // but you can upcast to one:
    @SuppressWarnings("unchecked")
    LinkedList<MapEntry<K,V>>[] buckets =
            new LinkedList[CAPACITY];
    public V put(K key, V value) {
        V oldValue = null;
        int index = Math.abs(key.hashCode()) % CAPACITY;
        if(buckets[index] == null)
            buckets[index] = new LinkedList<MapEntry<K,V>>();
        LinkedList<MapEntry<K,V>> bucket = buckets[index];
        MapEntry<K,V> pair = new MapEntry<K,V>(key, value);
        boolean found = false;
        ListIterator<MapEntry<K,V>> it = bucket.listIterator();
        while(it.hasNext()) {
            MapEntry<K,V> iPair = it.next();
            if(iPair.getKey().equals(key)) {
                oldValue = iPair.getValue();
                it.set(pair); // Replace old with new
                found = true;
                break;
            }
        }
        if(!found)
            buckets[index].add(pair);
        double newLoadFactor = size()/ CAPACITY;
        if( newLoadFactor > loadFactor )
            rehash();
        return oldValue;
    }
    public V get(Object key) {
        int index = Math.abs(key.hashCode()) % CAPACITY;
        if(buckets[index] == null) return null;
        for(MapEntry<K,V> iPair : buckets[index])
            if(iPair.getKey().equals(key))
                return iPair.getValue();
        return null;
    }
    public Set<Entry<K,V>> entrySet() {
        Set<Map.Entry<K,V>> set= new HashSet<Map.Entry<K,V>>();
        for(LinkedList<MapEntry<K,V>> bucket : buckets) {
            if(bucket == null) continue;
            for(MapEntry<K,V> mpair : bucket)
                set.add(mpair);
        }
        return set;
    }
    private void rehash() {
        CAPACITY = PrimeIntegerGenerator.findNextPrime( 2 * CAPACITY);
        LinkedList<MapEntry<K,V>>[] oldBuckets = buckets;
        buckets = new LinkedList[CAPACITY];
        for(LinkedList<MapEntry<K,V>> bucket : oldBuckets) {
            if(bucket == null) continue;
            for(MapEntry<K,V> mpair : bucket)
                this.put(mpair.getKey(), mpair.getValue());
        }
    }
    public int capacity() {
        return CAPACITY;
    }


    public static void main(String[] args) {
        RandomGenerator.Integer rgi = new RandomGenerator.Integer(10000);
        print("Testing map m of <String,String>: the basics:");
        SimpleHashMap39<String,String> m = new SimpleHashMap39<String,String>();
        m.putAll(Countries.capitals(10));
        print("m: " + m);
        print("m.get(\"CHAD\") " + m.get("CHAD"));
        print("m.size() = " + m.size());
        print("m.capacity() = " + m.capacity());
        print("Testing rehash() for a map m2 of <Integer,Integer>:");
        SimpleHashMap39<Integer,Integer> m2 = new SimpleHashMap39<Integer,Integer>();
        print("m2: " + m2);
        print("m2.size() = " + m2.size());
        print("m2.capacity() = " + m2.capacity());
        m2.putAll(MapData.map(rgi, rgi, 996));
        print("after m2.putAll(MapData.map(rgi, rgi, 996): ");
        print("m2.size() = " + m2.size());
        print("m2.capacity() = " + m2.capacity());
        m2.putAll(MapData.map(rgi, rgi, 2000));
        print("after m2.putAll(MapData.map(rgi, rgi, 2000): ");
        print("m2.size() = " + m2.size());
        print("m2.capacity() = " + m2.capacity());
        m2.putAll(MapData.map(rgi, rgi, 1500));
        print("after m2.putAll(MapData.map(rgi, rgi, 1500): ");
        print("m2.size() = " + m2.size());
        print("m2.capacity() = " + m2.capacity());
    }
}
public class Exercise39 {
    public static void main(String[] args) {
        SimpleHashMap39.main(null);
    }
}
