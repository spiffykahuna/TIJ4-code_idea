package containers;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.mindview.util.Print.print;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/29/12
 * Time: 5:33 PM
 * To change this template use File | Settings | File Templates.
 */
class CountedString27 {
    private static List<String> created =
		new ArrayList<String>();
	private String s;
	private int id = 0;
	public CountedString27(String str) {
		s = str;
		created.add(s);
		// id is the total number of instances
		// of this string in use by CountedString:
		for(String s2: created)
			if(s2.equals(s)) id++;
	}
	public String toString() {
		return "String: " + s + " id: " + id +
			" hashCode(): " + hashCode();
	}
	public int hashCode() {
		int result = 17;
		result = 37 * result + s.hashCode();
		// result = 37 * result + id;
		return result;
	}
	public boolean equals(Object o) {
		return o instanceof CountedString27 &&
			s.equals(((CountedString27)o).s) &&
			id == ((CountedString27)o).id;
	}
	public static void main(String[] args) {
		Map<CountedString27,Integer> map =
			new HashMap<CountedString27,Integer>();
		CountedString27[] cs = new CountedString27[5];
		for(int i = 0; i < cs.length; i++) {
			cs[i] = new CountedString27("hi");
			map.put(cs[i], i); // Autobox int -> Integer
		}
		print(map);
		// Problem: same hash code for different objects:
		for(CountedString27 cstring : cs) {
			print("Looking up " + cstring);
			print(map.get(cstring));
		}
	}


}

public class Exercise27 {
    public static void main(String[] args) {
        CountedString27.main(null);
    }
}
