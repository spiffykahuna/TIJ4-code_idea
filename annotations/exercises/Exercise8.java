package annotations.exercises;

import net.mindview.atunit.AtUnit;
import net.mindview.atunit.Test;
import net.mindview.atunit.TestProperty;

public class Exercise8  extends Exercise8Private {
    /**
     * You need to set JVM working directory location to folder where is Exercise.class
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        AtUnit.main(new String[]{"Exercise8"});
    }


    @Test
    void initialization() {
        protectedInit();
    }
}
