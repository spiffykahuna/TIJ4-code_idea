package containers;

import net.mindview.util.Countries;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2/2/12
 * Time: 11:01 AM
 * To change this template use File | Settings | File Templates.
 */
class SimpleHashMap37<K, V> extends AbstractMap<K,V> {
    // Choose a prime number for the hash table
    // size, to achieve a uniform distribution:
    static final int SIZE = 997;
    // You can't have a physical array of generics,
    // but you can upcast to one:
    @SuppressWarnings("unchecked")
    ArrayList<MapEntry<K,V>>[] buckets =
            new ArrayList[SIZE];
    public V put(K key, V value) {
        V oldValue = null;
        int index = Math.abs(key.hashCode()) % SIZE;
        if(buckets[index] == null)
            buckets[index] = new ArrayList<MapEntry<K,V>>();
        ArrayList<MapEntry<K,V>> bucket = buckets[index];
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
    public Set<Entry<K,V>> entrySet() {
        Set<Map.Entry<K,V>> set= new HashSet<Map.Entry<K,V>>();
        for(ArrayList<MapEntry<K,V>> bucket : buckets) {
            if(bucket == null) continue;
            for(MapEntry<K,V> mpair : bucket)
                set.add(mpair);
        }
        return set;
    }
    public static void main(String[] args) {
        SimpleHashMap37<String,String> m =
                new SimpleHashMap37<String,String>();
        m.putAll(Countries.capitals(25));
        System.out.println(m);
        System.out.println(m.get("ERITREA"));
        System.out.println(m.entrySet());
    }
}


public class Exercise37 {
    public static void main(String[] args) {
        SimpleHashMap37.main(null);
        Tester.defaultParams = TestParam.array(
                10, 5000, 100, 5000, 1000, 100);

        Tester.run(new SlowMap<Integer, Integer>(), MapPerformance.tests);
        Tester.run(new SlowMap36<Integer, Integer>(), MapPerformance.tests);
        Tester.run(new SimpleHashMap37<Integer, Integer>(), MapPerformance.tests);
        Tester.run(new HashMap<Integer, Integer>(), MapPerformance.tests);
        Tester.run(new TreeMap<Integer,Integer>(), MapPerformance.tests);
        Tester.run(new LinkedHashMap<Integer,Integer>(), MapPerformance.tests);
        Tester.run(new IdentityHashMap<Integer,Integer>(), MapPerformance.tests);
        Tester.run(new WeakHashMap<Integer,Integer>(), MapPerformance.tests);
        Tester.run(new Hashtable<Integer,Integer>(), MapPerformance.tests);


    }
}
