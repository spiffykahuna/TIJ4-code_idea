package annotations.exercises;

import net.mindview.atunit.AtUnit;
import net.mindview.atunit.Test;

import java.util.HashSet;
import java.util.Set;

public class Exercise5 extends HashSet<String> {
    /**
     * You need to set JVM working directory location to folder where is Exercise.class
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        AtUnit.main(new String[]{"Exercise5"});
    }

    @Test
    boolean initialization() {
        return isEmpty();
    }

    @Test boolean _contains() {
        return add("one") && contains("one");
    }
    @Test boolean _remove() {
        return add("one") && remove("one") && isEmpty();
    }

}
