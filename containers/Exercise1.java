package containers;


import net.mindview.util.Countries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/21/12
 * Time: 1:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class Exercise1 {
    public static void main(String[] args){
        List<String> countries = new ArrayList<String>(Countries.names());
        Collections.sort(countries);
        System.out.println(countries);

        List<String> countries2 = new LinkedList<String>(Countries.names());
        Collections.sort(countries2);
        System.out.println(countries2);

        for(int i=0; i < 3; i++){
            Collections.shuffle(countries);
            Collections.shuffle(countries2);
            System.out.println("First:  " + countries);
            System.out.println("Second: " + countries2);
        }

    }
}
