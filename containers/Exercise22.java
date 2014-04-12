package containers;

import net.mindview.util.Countries;

import java.util.LinkedList;

import static net.mindview.util.Print.print;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/27/12
 * Time: 6:50 PM
 * To change this template use File | Settings | File Templates.
 */
class SimpleHashMapExtended<K, V> extends SimpleHashMap<K, V> {
    public void clear() {
		for(LinkedList<MapEntry<K,V>> bucket : buckets)
			if(bucket != null) bucket.clear();
	}

	public V remove(Object key) {
		V v = null;
		if(this.get(key) != null || this.containsKey(key)) {
			int index = Math.abs(key.hashCode()) % SIZE;
			for(MapEntry<K,V> iPair : buckets[index])
				if(iPair.getKey().equals(key)) {
					v = iPair.getValue();
					int i =	buckets[index].indexOf(iPair);
					buckets[index].remove(i);
					break;
				}
        }
		return v;
	}

}
public class Exercise22 {
    public static void main(String[] args) {
        SimpleHashMapExtended<String,String> m =
			new SimpleHashMapExtended<String,String>();
		m.putAll(Countries.capitals(5));
		print(m);
		print(m.get("ALGERIA"));
		print(m.remove("ALGERIA"));
		print(m.get("ALGERIA"));
		print(m.remove("ANGOLA"));
		print(m);
		m.clear();
		print(m);

    }
}
