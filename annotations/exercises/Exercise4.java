package annotations.exercises;

import net.mindview.atunit.AtUnit;
import net.mindview.atunit.Test;

import java.util.HashSet;
import java.util.Set;

public class Exercise4 {

    /**
     * You need to set JVM working directory location to folder where is Exercise.class
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        AtUnit.main(new String[] {"Exercise4"});
    }

    HashSet<String> testObject = new HashSet<String>();
    static Set<HashSet<String>> usedObjects = new HashSet<HashSet<String>>();

    @Test
    boolean initialization() {
        return newInstance(testObject) && testObject.isEmpty();
    }

    public static boolean newInstance(HashSet<String> testObject) {
        for(HashSet<String> obj: usedObjects) {
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
