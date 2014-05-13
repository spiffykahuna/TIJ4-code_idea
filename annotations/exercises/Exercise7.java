package annotations.exercises;

import net.mindview.atunit.AtUnit;
import net.mindview.atunit.Test;

import java.util.HashSet;
import java.util.LinkedList;

public class Exercise7 extends LinkedList<String> {
    /**
     * You need to set JVM working directory location to folder where is Exercise.class
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        AtUnit.main(new String[]{"Exercise7"});
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
