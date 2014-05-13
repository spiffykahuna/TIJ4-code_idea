package annotations.exercises;

import net.mindview.atunit.AtUnit;
import net.mindview.atunit.Test;

import java.util.HashMap;

public class Exercise9 extends HashMap<Integer, String> {
    @Test
    boolean _put() {
        put(1, "Hi");
        return get(1).equals("Hi");
    }

    @Test
    boolean _containsKey() {
        put(2, "Bye");
        return containsKey(2);
    }

    @Test
    boolean _containsValue() {
        put(3, "yes");
        return containsValue("yes");
    }

    @Test
    boolean _get() {
        put(4, "hello");
        return get(4).equals("hello");
    }

    @Test
    boolean _size() {
        return size() == 0;
    }

    @Test
    boolean _clear() {
        put(5, "so long");
        clear();
        return size() == 0;
    }

    public static void main(String[] args) throws Exception {
        AtUnit.main(new String[]{"Exercise9"});
    }
}
