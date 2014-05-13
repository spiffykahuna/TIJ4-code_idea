package annotations.exercises;

import net.mindview.atunit.AtUnit;
import net.mindview.atunit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Exercise6 {

    /**
     * You need to set JVM working directory location to folder where is Exercise.class
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        AtUnit.main(new String[] {"Exercise6"});
    }

    List<String> testObject = new LinkedList<String>();
    static Set<List<String>> usedObjects = new HashSet<List<String>>();

    @Test
    boolean initialization() {
        return newInstance(testObject) && testObject.isEmpty();
    }

    public static boolean newInstance(List<String> testObject) {
        for(List<String> obj: usedObjects) {
            if(obj == testObject) {
                return false;
            }
        }
        usedObjects.add(testObject);
        return true;
    }

    @Test boolean _contains() {
        testObject.add("one");
        return testObject.contains("one") && newInstance(testObject);
    }
    @Test boolean _remove() {
        testObject.add("one");
        testObject.remove("one");
        return testObject.isEmpty() && newInstance(testObject);
    }
}
