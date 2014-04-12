package containers;

import net.mindview.util.Countries;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/25/12
 * Time: 11:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class Exercise3 {
    public static void main(String[] args){
        Set<String> countriesHashSet = new HashSet<String>();
        Set<String> countriesLinkedHashSet = new LinkedHashSet<String>();
        Set<String> countriesTreeSet = new TreeSet<String>();

        for(int i = 0; i < 10; i++){
            countriesHashSet.addAll(Countries.names());
            countriesLinkedHashSet.addAll(Countries.names());
            countriesTreeSet.addAll(Countries.names());
        }

        System.out.println("HashSet: (" + countriesHashSet.size() + ")  " + countriesHashSet);
        System.out.println("LinkedHashSet: (" + countriesLinkedHashSet.size() + ")  " + countriesLinkedHashSet);
        System.out.println("TreeSet: (" + countriesTreeSet.size() + ")  " + countriesTreeSet);
    }
}
