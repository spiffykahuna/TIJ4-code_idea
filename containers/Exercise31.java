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

class StringList31 implements  List<String>{
    String[] array = new String[10];
    int currentIndex = 0;

    
    public String get(int index) {
        if(index < 0) throw new IndexOutOfBoundsException("Index should be positive");
        if(index > currentIndex) throw new IndexOutOfBoundsException("Index is greater than array size");
        return array[index];
    }

    public String set(int i, String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void add(int i, String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String remove(int i) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int indexOf(Object o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int lastIndexOf(Object o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ListIterator<String> listIterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ListIterator<String> listIterator(int i) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<String> subList(int i, int i1) {
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

    public Iterator<String> iterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object[] toArray() {
        return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public <T> T[] toArray(T[] ts) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean add(String newString) {
        // resize if there is no space anymore
        if(currentIndex == array.length) {
//            String[] newArray = new String[array.length + 1];
//            for(int i = 0; i < array.length; i++)
//                newArray[i] = array[i];
//            array = newArray;
            array = Arrays.copyOf(array, (array.length * 3)/2 + 1);
        }
        if (newString == null) throw new NullPointerException("String was null");
        array[currentIndex++] = newString;

        return true;
    }

    public boolean remove(Object o) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean containsAll(Collection<?> objects) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean addAll(Collection<? extends String> strings) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean addAll(int i, Collection<? extends String> strings) {
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

class StringsHolder {
    private int size = 0;
    private String[] sArray = new String[size];
    // Can add only Strings:
    // (Resizing and copying will degrade performance)
    public void add(String s) {
        size += 1;
        String[] temp = new String[size];
        for(int i = 0; i < sArray.length; i++) temp[i] = sArray[i];
        temp[size - 1] = s;
        sArray = temp;
    }
    // get() returns only Strings:
    public String get(int i) {
        if(-1 < i && i < size) return sArray[i];
        else throw new ArrayIndexOutOfBoundsException(i);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < sArray.length; i++) sb.append((sArray[i] + " "));
        return sb.toString();
    }
}

// Alternate to eliminate resizing time for fixed size StringsHolder2:
class StringsHolder2 {
    private int size = 1000;
    private int index = 0;
    private String[] sArray = new String[size];
    // Can add only Strings:
    public void add(String s) {
        if(index < size) sArray[index++] = s;
    }
    // get() returns only Strings:
    public String get(int i) {
        if(-1 < i && i < size) return sArray[i];
        else throw new ArrayIndexOutOfBoundsException(i);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < sArray.length; i++) sb.append((sArray[i] + " "));
        return sb.toString();
    }
}

class Ex31 {
    static long addTimeTest(StringsHolder sh, int reps) {
        long start = System.nanoTime();
        for(int i = 0; i < reps; i++) sh.add("hi");
        long stop = System.nanoTime();
        return (stop - start)/reps;
    }
    static long addTimeTest(ArrayList<String> list, int reps) {
        long start = System.nanoTime();
        for(int i = 0; i < reps; i++) list.add("hi");
        long stop = System.nanoTime();
        return (stop - start)/reps;
    }
    static long getTimeTest(StringsHolder sh, int reps) {
        long start = System.nanoTime();
        for(int i = 0; i < reps; i++) sh.get(i);
        long stop = System.nanoTime();
        return (stop - start)/reps;
    }
    static long getTimeTest(ArrayList<String> list, int reps) {
        long start = System.nanoTime();
        for(int i = 0; i < reps; i++) list.get(i);
        long stop = System.nanoTime();
        return (stop - start)/reps;
    }
    static long addTimeTest(StringsHolder2 sh, int reps) {
        long start = System.nanoTime();
        for(int i = 0; i < reps; i++) sh.add("hi");
        long stop = System.nanoTime();
        return (stop - start)/reps;
    }
    static long getTimeTest(StringsHolder2 sh, int reps) {
        long start = System.nanoTime();
        for(int i = 0; i < reps; i++) sh.get(i);
        long stop = System.nanoTime();
        return (stop - start)/reps;
    }
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        StringsHolder sh = new StringsHolder();
        print("Add times (nanoseconds):");
        print("Mean of 1000:");
        print("ArrayList add(): " + addTimeTest(list, 1000));
        print("StringsHolder add(): " + addTimeTest(sh, 1000));
        print("Mean of 10000:");
        print("ArrayList add(): " + addTimeTest(list, 10000));
        print("StringsHolder add(): " + addTimeTest(sh, 10000));
        print();
        print("Get times (nanoseconds):");
        print("Mean of 10000:");
        print("ArrayList get(): " + getTimeTest(list, 10000));
        print("StringsHolder get(): " + getTimeTest(sh, 10000));
        print();
        print("Using alternate fixed size (1000) StringsHolder2");
        print("eliminates resizing and copying:");
        ArrayList<String> list2 = new ArrayList<String>();
        StringsHolder2 sh2 = new StringsHolder2();
        print("Add times (nanoseconds):");
        print("Mean of 1000:");
        print("ArrayList add(): " + addTimeTest(list2, 1000));
        print("StringsHolder2 add(): " + addTimeTest(sh2, 1000));
        print();
        print("Get times (nanoseconds):");
        print("Mean of 1000:");
        print("ArrayList get(): " + getTimeTest(list2, 1000));
        print("StringsHolder2 get(): " + getTimeTest(sh2, 1000));
    }
}

public class Exercise31 {

    static Generator<String> stGen  = new RandomGenerator.String();
    static Random rand = new Random();

    public static long addTest(int reps, List<String> coll) {

        long start = System.nanoTime();
        for(int i = 0; i < reps; i++)
            coll.add("test");
        return (System.nanoTime() - start)/reps;
    }
    public static long getTest(int reps, int arraySize,  List<String> coll) {
        for(int i = 0; i < arraySize; i++)
            coll.add(stGen.next());

        long start = System.nanoTime();
        for(int i = 0; i < reps; i++)
            coll.get(rand.nextInt(arraySize));
        return (System.nanoTime() - start)/reps;
    }

    public static void main(String[] args) {
        StringList31 strList = new StringList31();
        strList.add("hehe");
        for(int i = 0; i < 100; i++)
            strList.add("test");
        print("StrList: size(" + strList.size() + ") items: " + strList);
        print("StringList 10 adds: " + addTest(10, new StringList31()));
        print("ArrayList 10 adds: " + addTest(10, new ArrayList<String>()));
        print();
        print("StringList 100 adds: " + addTest(100, new StringList31()));
        print("ArrayList 100 adds: " + addTest(100, new ArrayList<String>()));
        print();
        print("StringList 1000 adds: " + addTest(1000, new StringList31()));
        print("ArrayList 1000 adds: " + addTest(1000, new ArrayList<String>()));
        print();
        print("StringList 10 gets: " + getTest(10,10, new StringList31()));
        print("ArrayList 10 gets: " + getTest(10,10, new ArrayList<String>()));
        print();
        print("StringList 100 gets: " + getTest(100, 1000, new StringList31()));
        print("ArrayList 100 gets: " + getTest(100, 1000, new ArrayList<String>()));
        print();
        print("StringList 1000 adds: " + getTest(1000, 10000, new StringList31()));
        print("ArrayList 1000 adds: " + getTest(1000, 10000, new ArrayList<String>()));
        
        print();
        Ex31.main(null);

    }
}
