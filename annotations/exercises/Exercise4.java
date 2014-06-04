package annotations.exercises;


import net.mindview.atunit.AtUnit;
import net.mindview.atunit.Test;

import java.util.HashSet;
import java.util.Set;

class Pojo {
    String one;
    int two;
    double three;

    String getOne() {
        return one;
    }

    void setOne(String one) {
        this.one = one;
    }

    int getTwo() {
        return two;
    }

    void setTwo(int two) {
        this.two = two;
    }

    double getThree() {
        return three;
    }

    void setThree(double three) {
        this.three = three;
    }
}

public class Exercise4 {
    Pojo pojo = new Pojo();

    static Set<Pojo> objects = new HashSet<Pojo>();

    @Test public boolean one() {  return assertNew(pojo); }
    @Test public boolean two() {  return assertNew(pojo); }
    @Test public boolean three() {  return assertNew(pojo); }

    private boolean assertNew(Pojo pojo) {
        if(!objects.add(pojo))
            throw new IllegalArgumentException("Is not new");
        return true;
    }

    public static void main(String[] args) throws Exception {
        AtUnit.main(new String[]{"out/production/TIJ4-code_idea/annotations/exercises/Exercise4"});
    }
}
