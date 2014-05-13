package annotations.exercises;

import net.mindview.atunit.Test;
import net.mindview.atunit.TestProperty;

public class Exercise8Private {

    private void privateInit() {
        System.out.println("Exercise8Private.privateInit");
    }

    @TestProperty
    protected void protectedInit() {
        privateInit();
    }

}
