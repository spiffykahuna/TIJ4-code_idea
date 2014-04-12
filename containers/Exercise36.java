package containers;

import net.mindview.util.Countries;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2/1/12
 * Time: 11:08 PM
 * To change this template use File | Settings | File Templates.
 */
class SlowMap36<K,V> extends AbstractMap<K,V> {
    private List<MapEntry<K, V> > values = new ArrayList<MapEntry<K, V>>();
    public V put(K key, V value) {
        V oldValue = null; //= get(key); // The old value or null
        MapEntry<K, V>  newValue = new MapEntry<K, V>( key, value);
        int index = Collections.binarySearch(values, newValue, entryComparator);
        if( index < 0 || index > values.size()) {
            values.add(newValue);
        } else {
            oldValue = values.get(index).getValue();
            values.set(index, newValue);
        }
//        if(!values.contains(newValue)) {
//            values.add(newValue);
//        } else
//            values.set(values.indexOf(newValue), newValue);
        sort();
        return oldValue;
    }
    public V get(Object key) { // key is type Object, not K
        MapEntry<K, V>  value = new MapEntry<K, V>( (K) key, null);
        int index = Collections.binarySearch(values, value, entryComparator); // values.indexOf(value); //
        if( index < 0 || index > values.size())
            return null;
        return values.get(index).getValue();
    }
    public Set<Map.Entry<K,V> > entrySet() {
        Set<Map.Entry<K,V> > set= new LinkedHashSet<Map.Entry<K,V> >(values);
        return set;
    }
    public void sort() {
        Collections.sort(values, entryComparator);
    }
    Comparator<MapEntry<K, V> > entryComparator = new Comparator<MapEntry<K, V>>() {
        public int compare(MapEntry<K, V> a, MapEntry<K, V> b) {
            K aKey = a.getKey();
            K bKey = b.getKey();
            return new CompareToBuilder().append(aKey, bKey).toComparison();
        }
    };

    public void clear() {
        values.clear();
    }


    public static void main(String[] args) {
        SlowMap36<String,String> m= new SlowMap36<String,String>();
        m.putAll(Countries.capitals(15));
        m.putAll(Countries.capitals(15));
        System.out.println(m);
        System.out.println(m.get("BULGARIA"));
        System.out.println(m.entrySet());
    }
}

public class Exercise36 {
    public static void main(String[] args) {
        SlowMap36.main(null);
        SlowMap.main(null);
        Tester.defaultParams = TestParam.array(
                10, 5000, 100, 5000, 1000, 100);

        Tester.run(new SlowMap<Integer, Integer>(), MapPerformance.tests);
        Tester.run(new SlowMap36<Integer, Integer>(), MapPerformance.tests);
        Tester.run(new HashMap<Integer, Integer>(), MapPerformance.tests);
    }
}
