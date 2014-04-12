package containers;

import net.mindview.util.CountingMapData;
import net.mindview.util.RandomGenerator;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/25/12
 * Time: 11:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class Exercise5 extends AbstractMap<Integer,String> {
  private int size;
  private static String[] chars =
    "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
    .split(" ");

  public Exercise5(int size) {
    if(size < 0) this.size = 0;
    this.size = size;
  }

  private int getSize() { return this.size;}

  private static class Entry
  implements Map.Entry<Integer,String> {
    int index;
    Entry(int index) { this.index = index; }
    public boolean equals(Object o) {
      return Integer.valueOf(index).equals(o);
    }
    public Integer getKey() { return index; }
    public String getValue() {
      return
        chars[index % chars.length] +
        Integer.toString(index / chars.length);
    }
    public String setValue(String value) {
      throw new UnsupportedOperationException();
    }
    public int hashCode() {
      return Integer.valueOf(index).hashCode();
    }
  }

  // Use AbstractSet by implementing size() & iterator()
  static class EntrySet
    extends AbstractSet<Map.Entry<Integer,String>> {
      private int size;
      EntrySet(int size) {
        if(size < 0)
          this.size = 0;
        // Can't be any bigger than the array:
//        else if(size > chars.length)
//          this.size = chars.length;
        else
          this.size = size;
      }
      public int size() { return size; }

      private class Iter
      implements Iterator<Map.Entry<Integer,String>> {
        // Only one Entry object per Iterator:
        private Entry entry = new Entry(-1);
        public boolean hasNext() {
          return entry.index < size - 1;
        }
        public Map.Entry<Integer,String> next() {
          entry.index++;
          return entry;
        }
        public void remove() {
          throw new UnsupportedOperationException();
        }
      }
      public
      Iterator<Map.Entry<Integer,String>> iterator() {
        return new Iter();
      }
    }

  public Set<Map.Entry<Integer,String>> entrySet() {
    return new EntrySet(this.size);
  }


  public static void main(String[] args) {
    System.out.println(new Exercise5(60));
  }
}

