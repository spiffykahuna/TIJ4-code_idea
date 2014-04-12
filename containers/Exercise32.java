package containers;

import net.mindview.util.Generator;
import net.mindview.util.RandomGenerator;

import java.util.*;

import static net.mindview.util.Print.print;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2/1/12
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 */

class IntegerList32 implements  List<Integer>{
    Integer[] array = new Integer[10];
    int currentIndex = 0;


    public Integer get(int index) {
        if(index < 0) throw new IndexOutOfBoundsException("Index should be positive");
        if(index > currentIndex) throw new IndexOutOfBoundsException("Index is greater than array size");
        return array[index];
    }

    public Integer set(int index, Integer newValue) {
        if(index < 0) throw new IndexOutOfBoundsException("Index should be positive");
        if(index > currentIndex) throw new IndexOutOfBoundsException("Index is greater than array size");
        Integer oldValue = array[index];
        array[index] = newValue;
        return oldValue;
    }

    public void add(int i, Integer s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Integer remove(int i) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int indexOf(Object o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int lastIndexOf(Object o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ListIterator<Integer> listIterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ListIterator<Integer> listIterator(int i) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Integer> subList(int i, int i1) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int size() {
        return currentIndex;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isEmpty() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean contains(Object o) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Iterator<Integer> iterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object[] toArray() {
        return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public <T> T[] toArray(T[] ts) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean add(Integer newInt) {
        // resize if there is no space anymore
        if(currentIndex == array.length) {
//            String[] newArray = new String[array.length + 1];
//            for(int i = 0; i < array.length; i++)
//                newArray[i] = array[i];
//            array = newArray;
            array = Arrays.copyOf(array, (array.length * 3)/2 + 1);
        }
        if (newInt == null) throw new NullPointerException("String was null");
        array[currentIndex++] = newInt;

        return true;
    }

    public boolean remove(Object o) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean containsAll(Collection<?> objects) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean addAll(Collection<? extends Integer> strings) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean addAll(int i, Collection<? extends Integer> strings) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean removeAll(Collection<?> objects) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean retainAll(Collection<?> objects) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void clear() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String toString() {
        return Arrays.asList(Arrays.copyOf(array, currentIndex)).toString();
    }

}

class IntegersHolder {
    private int size = 0;
    private Integer[] sArray = new Integer[size];
    // Can add only Strings:
    // (Resizing and copying will degrade performance)
    public void add(Integer s) {
        size += 1;
        Integer[] temp = new Integer[size];
        for(int i = 0; i < sArray.length; i++) temp[i] = sArray[i];
        temp[size - 1] = s;
        sArray = temp;
    }
    // get() returns only Strings:
    public Integer get(int i) {
        if(-1 < i && i < size) return sArray[i];
        else throw new ArrayIndexOutOfBoundsException(i);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < sArray.length; i++) sb.append((sArray[i] + " "));
        return sb.toString();
    }
}

// Alternate to eliminate resizing time for fixed size IntegersHolder2:
class IntegersHolder2 {
    private int size = 1000;
    private int index = 0;
    private Integer[] sArray = new Integer[size];
    // Can add only Strings:
    public void add(Integer s) {
        if(index < size) sArray[index++] = s;
    }
    // get() returns only Strings:
    public Integer get(int i) {
        if(-1 < i && i < size) return sArray[i];
        else throw new ArrayIndexOutOfBoundsException(i);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < sArray.length; i++) sb.append((sArray[i] + " "));
        return sb.toString();
    }
}

class Ex32 {
    static long addTimeTest(IntegersHolder sh, int reps) {
        long start = System.nanoTime();
        for(int i = 0; i < reps; i++) sh.add(666);
        long stop = System.nanoTime();
        return (stop - start)/reps;
    }
    static long addTimeTest(ArrayList<Integer> list, int reps) {
        long start = System.nanoTime();
        for(int i = 0; i < reps; i++) list.add(666);
        long stop = System.nanoTime();
        return (stop - start)/reps;
    }
    static long getTimeTest(IntegersHolder sh, int reps) {
        long start = System.nanoTime();
        for(int i = 0; i < reps; i++) sh.get(i);
        long stop = System.nanoTime();
        return (stop - start)/reps;
    }
    static long getTimeTest(ArrayList<Integer> list, int reps) {
        long start = System.nanoTime();
        for(int i = 0; i < reps; i++) list.get(i);
        long stop = System.nanoTime();
        return (stop - start)/reps;
    }
    static long addTimeTest(IntegersHolder2 sh, int reps) {
        long start = System.nanoTime();
        for(int i = 0; i < reps; i++) sh.add(666);
        long stop = System.nanoTime();
        return (stop - start)/reps;
    }
    static long getTimeTest(IntegersHolder2 sh, int reps) {
        long start = System.nanoTime();
        for(int i = 0; i < reps; i++) sh.get(i);
        long stop = System.nanoTime();
        return (stop - start)/reps;
    }
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        IntegersHolder sh = new IntegersHolder();
        print("Add times (nanoseconds):");
        print("Mean of 1000:");
        print("ArrayList add(): " + addTimeTest(list, 1000));
        print("IntegersHolder add(): " + addTimeTest(sh, 1000));
        print("Mean of 10000:");
        print("ArrayList add(): " + addTimeTest(list, 10000));
        print("IntegersHolder add(): " + addTimeTest(sh, 10000));
        print();
        print("Get times (nanoseconds):");
        print("Mean of 10000:");
        print("ArrayList get(): " + getTimeTest(list, 10000));
        print("IntegersHolder get(): " + getTimeTest(sh, 10000));
        print();
        print("Using alternate fixed size (1000) IntegersHolder2");
        print("eliminates resizing and copying:");
        ArrayList<Integer> list2 = new ArrayList<Integer>();
        IntegersHolder2 sh2 = new IntegersHolder2();
        print("Add times (nanoseconds):");
        print("Mean of 1000:");
        print("ArrayList add(): " + addTimeTest(list2, 1000));
        print("IntegersHolder2 add(): " + addTimeTest(sh2, 1000));
        print();
        print("Get times (nanoseconds):");
        print("Mean of 1000:");
        print("ArrayList get(): " + getTimeTest(list2, 1000));
        print("IntegersHolder2 get(): " + getTimeTest(sh2, 1000));
    }
}

public class Exercise32 {


    static Random rand = new Random();

    public static long addTest(int reps, List<Integer> coll) {

        long start = System.nanoTime();
        for(int i = 0; i < reps; i++)
            coll.add(666);
        return (System.nanoTime() - start)/reps;
    }
    public static long getTest(int reps, int arraySize,  List<Integer> coll) {
        for(int i = 0; i < arraySize; i++)
            coll.add(rand.nextInt());

        long start = System.nanoTime();
        for(int i = 0; i < reps; i++)
            coll.get(rand.nextInt(arraySize));
        return (System.nanoTime() - start)/reps;
    }

    public static long incTest(int reps, int arraySize,  List<Integer> coll) {
        for(int i = 0; i < arraySize; i++)
            coll.add(rand.nextInt());

        long start = System.nanoTime();
        for(int i = 0; i < reps; i++) {
            int index =   rand.nextInt(arraySize);
            coll.set(index, coll.get(index) + 1);
        }
            
        return (System.nanoTime() - start)/reps;
    }

    public static void main(String[] args) {
        IntegerList32 strList = new IntegerList32();
        strList.add(1);
        for(int i = 0; i < 100; i++)
            strList.add(666);
        print("StrList: size(" + strList.size() + ") items: " + strList);
        print("StringList 10 adds: " + addTest(10, new IntegerList32()));
        print("ArrayList 10 adds: " + addTest(10, new ArrayList<Integer>()));
        print();
        print("StringList 100 adds: " + addTest(100, new IntegerList32()));
        print("ArrayList 100 adds: " + addTest(100, new ArrayList<Integer>()));
        print();
        print("StringList 1000 adds: " + addTest(1000, new IntegerList32()));
        print("ArrayList 1000 adds: " + addTest(1000, new ArrayList<Integer>()));
        print();
        print("StringList 10 gets: " + getTest(10,10, new IntegerList32()));
        print("ArrayList 10 gets: " + getTest(10,10, new ArrayList<Integer>()));
        print();
        print("StringList 100 gets: " + getTest(100, 1000, new IntegerList32()));
        print("ArrayList 100 gets: " + getTest(100, 1000, new ArrayList<Integer>()));
        print();
        print("StringList 1000 gets: " + getTest(1000, 10000, new IntegerList32()));
        print("ArrayList 1000 gets: " + getTest(1000, 10000, new ArrayList<Integer>()));

        print();
        print("StringList 10 incs: " + incTest(10,10, new IntegerList32()));
        print("ArrayList 10 incs: " + incTest(10,10, new ArrayList<Integer>()));
        print();
        print("StringList 100 incs: " + incTest(100, 1000, new IntegerList32()));
        print("ArrayList 100 incs: " + incTest(100, 1000, new ArrayList<Integer>()));
        print();
        print("StringList 1000 incs: " + incTest(1000, 10000, new IntegerList32()));
        print("ArrayList 1000 incs: " + incTest(1000, 10000, new ArrayList<Integer>()));

        print();
        Ex32.main(null);

    }
}
