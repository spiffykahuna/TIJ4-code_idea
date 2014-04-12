package containers;

import net.mindview.util.CountingMapData;

import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/27/12
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */


class SlowMapFixed<K, V> extends AbstractMap<K,V> {
    private List<K> keys = new ArrayList<K>();
	private List<V> values = new ArrayList<V>();
    private EntrySet entries = new EntrySet();
	public Set<Map.Entry<K,V> > entrySet() { return entries; }

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

				public Map.Entry<K,V> next() {
					int i = ++index;
					return new MapEntry<K,V>(
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
public class Exercise16 {
    public static void main(String[] args) {
        Maps.test(new SlowMap<Integer, String>());
        Maps.test(new HashMap<Integer, String> ());
        Maps.test(new SlowMapFixed<Integer, String>());

    }
}
