package containers;

import net.mindview.util.Countries;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/25/12
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class Exercise7 {
    public static void main(String[] args) {
        List<String> countries = Countries.names();
        List<String> al = new ArrayList<String>(countries);
        List<String> ll = new LinkedList<String>(countries);

        printList(al.iterator());
        printList(ll.iterator());
        ListIterator<String> lit1 = al.listIterator();
        ListIterator<String> lit2 = ll.listIterator();
        while(lit1.hasNext()) {
            lit1.next();
            if(lit2.hasNext())
                lit1.set(lit2.next());

        }
        System.out.println(al);

        lit1 = al.listIterator(al.size());
        lit2 = ll.listIterator(ll.size());
        while(lit1.hasPrevious()) {
            lit1.previous();
            if(lit2.hasPrevious())
                lit1.set(lit2.previous());

        }
        System.out.println(al);





    }

    private static void printList(Iterator<String> it) {

        while(it.hasNext()){
            System.out.print(it.next() + " ");
        }
        System.out.print("\n");
    }
}
