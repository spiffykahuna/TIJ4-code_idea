package containers;

import org.apache.commons.lang3.builder.EqualsBuilder;
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
 * Time: 5:14 PM
 * To change this template use File | Settings | File Templates.
 */

class CountedStringWithChar {
  protected static List<String> created =
          new ArrayList<String>();
  protected String s;
  protected int id = 0;
  protected char chr;

  public CountedStringWithChar(String str, char chr) {
    s = str;
    this.chr = chr;
    created.add(s);
    // id is the total number of instances
    // of this string in use by CountedString:
    for(String s2 : created)
      if(s2.equals(s))
        id++;
  }
  public String toString() {
    return "String: " + s + " id: " + id + " char: " + chr +
      " hashCode(): " + hashCode();
  }
  public int hashCode() {
    // The very simple approach:
    // return s.hashCode() * id;
    // Using Joshua Bloch's recipe:
    return new HashCodeBuilder(17, 37)
            .append(s)
            .append(id)
            .append(chr)
            .toHashCode();
  }
  public boolean equals(Object obj) {
      if (obj == null) { return false; }
      if (obj == this) { return true; }
      if (obj.getClass() != getClass()) {
        return false;
        }
      CountedStringWithChar rhs = (CountedStringWithChar) obj;
      return new EqualsBuilder()
                 .append(s, rhs.s)
                 .append(id, rhs.id)
                 .append(chr, rhs.chr)
                 .isEquals();
  }
  public static void main(String[] args) {
    Map<CountedStringWithChar,Integer> map =
      new HashMap<CountedStringWithChar,Integer>();
    CountedStringWithChar[] cs = new CountedStringWithChar[5];
    for(int i = 0; i < cs.length; i++) {
      cs[i] = new CountedStringWithChar("hi", 'e');
      map.put(cs[i], i); // Autobox int -> Integer
    }
    print(map);
    for(CountedStringWithChar cstring : cs) {
      print("Looking up " + cstring);
      print(map.get(cstring));
    }
  }
}


public class Exercise26 {
    public static void main(String[] args) {
        CountedStringWithChar.main(null);
    }
}
