package containers;

import net.mindview.util.Countries;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/25/12
 * Time: 9:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class Exercise2 {
    public static void main(String[] args){

        long t0 = System.nanoTime();


        Map<String,String> capsMap = new HashMap<String, String>();
        Set<String> capsSet = new HashSet<String>();

        for(Map.Entry<String,String> country: Countries.capitals().entrySet()){
            if(country.getKey().startsWith("A")) {
                capsMap.put(country.getKey(), country.getValue());
                capsSet.add(country.getKey());
            }
        }

        double elapsedTime = (System.nanoTime() - t0);
        System.out.println(capsMap);
        System.out.println(capsSet);
        System.out.println("Elapsed time: " + elapsedTime);


        t0 = System.nanoTime();
        Map<String,String> hm = new HashMap<String,String>();
		Set<String> hs = hm.keySet();
		Pattern p = Pattern.compile("A[a-zA-Z]*");
		for(int i = 0; i < Countries.DATA.length; i++) {
			if(p.matcher(Countries.DATA[i][0]).matches())
				hm.put(Countries.DATA[i][0], Countries.DATA[i][1]);
		}
        elapsedTime = (System.nanoTime() - t0);
        System.out.println(hm);
        System.out.println(hs);
        System.out.println("Elapsed time: " + elapsedTime);
    }
}
