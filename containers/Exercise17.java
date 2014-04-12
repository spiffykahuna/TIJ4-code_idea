package containers;

import net.mindview.util.Countries;

import java.util.*;

import static net.mindview.util.Print.print;


/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/27/12
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 */

class SlowMapExtended<K,V> implements Map<K,V> {
    private List<K> keys = new ArrayList<K>();
	private List<V> values = new ArrayList<V>();
	private EntrySet entries = new EntrySet();
	private Set<K> keySet = new KeySet();
	public Set<Map.Entry<K,V>> entrySet() { return entries; }
	public Set<K> keySet() { return keySet; }
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
	public void clear() {
		keys.clear();
		values.clear();
	}
	public boolean containsKey(Object key) {
		return keys.contains(key);
	}
	public boolean containsValue(Object value) {
		return values.contains(value);
	}
//	public boolean equals(Object o) {
//		if(o instanceof SlowMapExtended) {
//			if(this.entrySet().equals(((SlowMapExtended)o).entrySet()))
//				return true;
//		}
//		return false;
//	}
    // from OpenJdk
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Map))
            return false;
        Map<K,V> m = (Map<K,V>) o;
        if (m.size() != size())
            return false;

        try {
            Iterator<Entry<K,V>> i = entrySet().iterator();
            while (i.hasNext()) {
                Entry<K,V> e = i.next();
                K key = e.getKey();
                V value = e.getValue();
                if (value == null) {
                    if (!(m.get(key)==null && m.containsKey(key)))
                        return false;
                } else {
                    if (!value.equals(m.get(key)))
                        return false;
                }
            }
        } catch (ClassCastException unused) {
            return false;
        } catch (NullPointerException unused) {
            return false;
        }

        return true;
    }

	public int hashCode() {
		return this.entrySet().hashCode();
	}
	public boolean isEmpty() {
		return this.entrySet().isEmpty();
	}
	private class KeySet extends AbstractSet<K> {
		public int size() { return keys.size(); }
		public Iterator<K> iterator() {
			return new Iterator<K>() {
				private int index = -1;
				public boolean hasNext() {
					return index < keys.size() - 1;
				}
				public K next() {
					int i = ++index;
					return keys.get(index);
				}
				public void remove() {
					keys.remove(index--);
    				}
			};
		}

	}
	public void putAll(Map<? extends K,? extends V> m) {
		for(Map.Entry<? extends K,? extends V> me : m.entrySet())
			this.put(me.getKey(), me.getValue());
	}
	public V remove(Object key) {
		V v = this.get(key);
		int i = keys.indexOf(key);
		keys.remove(i);
		values.remove(i);
		return v;
	}
	public int size() { return keys.size(); }
	public Collection<V> values() {
		return values;
	}
	public String toString() {
		return this.entrySet().toString();
	}
}
public class Exercise17 {

    public static void main(String[] args) {
        SlowMapExtended<String,String> m = new SlowMapExtended<String,String>();
		m.putAll(Countries.capitals(15));
		print("m: " + m);
		print("m.get(\"BURUNDI\"): " + m.get("BURUNDI"));
		print("m.entrySet(): " + m.entrySet());
		print("m.keySet(): " + m.keySet());
		print("m.values() = " + m.values());
		print("Two different maps: ");
		SlowMapExtended<String,String> m2 = new SlowMapExtended<String,String>();
		print("m.equals(m2): " + m.equals(m2));
		m2.putAll(Countries.capitals(15));
		print("Maps with same entries: ");
		print("m.equals(m2): " + m.equals(m2));
		m.clear();
		print("After m.clear(), m.isEmpty(): " +
			m.isEmpty() + ", m = " + m);
		m2.keySet().clear();
		print("After m2.keySet().clear(), m2.isEmpty(): "
			+ m2.isEmpty() + ", m2 = " + m2);
    }
}
