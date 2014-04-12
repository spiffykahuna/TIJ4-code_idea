package containers;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/25/12
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class Exercise6 {
    static void test(String msg, List<String> list) {
        System.out.println("--- " + msg + " ---");
        List<String> c = list;
        List<String> subList = list.subList(1,8);
        // Copy of the sublist:
        List<String> c2 = new ArrayList<String>(subList);
        try { c.retainAll(c2); } catch(Exception e) {
          System.out.println("retainAll(): " + e);
        }
        try { c.removeAll(c2); } catch(Exception e) {
          System.out.println("removeAll(): " + e);
        }
        try { c.clear(); } catch(Exception e) {
          System.out.println("clear(): " + e);
        }
        try { c.add("X"); } catch(Exception e) {
          System.out.println("add(): " + e);
        }
        try { c.addAll(c2); } catch(Exception e) {
          System.out.println("addAll(): " + e);
        }
        try { c.remove("C"); } catch(Exception e) {
          System.out.println("remove(): " + e);
        }
        // The List.set() method modifies the value but
        // doesn't change the size of the data structure:
        try {
          list.set(0, "X");
        } catch(Exception e) {
          System.out.println("List.set(): " + e);
        }
        // difference between Collection and List
        try { c.add(2,"Z"); } catch(Exception e) {
          System.out.println("add(index, element): " + e);
        }
        try { c.addAll(1,c2); } catch(Exception e) {
          System.out.println("addAll(int index, Collection<? extends E> c): " + e);
        }
        try { c.get(2); } catch(Exception e) {
          System.out.println("get(int index): " + e);
        }
        try { c.indexOf("Z"); } catch(Exception e) {
          System.out.println("indexOf(Object o): " + e);
        }
        try { c.lastIndexOf("Z"); } catch(Exception e) {
          System.out.println("lastIndexOf(Object o): " + e);
        }
        try { c.subList(2, 5); } catch(Exception e) {
          System.out.println("subList(int fromIndex, int toIndex): " + e);
        }
  }

  public static void main(String[] args) {
    List<String> list =
      Arrays.asList("A B C D E F G H I J K L".split(" "));
    test("Modifiable Copy", new ArrayList<String>(list));
    test("Arrays.asList()", list);
    test("unmodifiableList()",
      Collections.unmodifiableList(
              new ArrayList<String>(list)));
  }
}
