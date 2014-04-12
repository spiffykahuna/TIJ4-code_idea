package containers;

import net.mindview.util.Generator;
import net.mindview.util.RandomGenerator;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.*;

import static net.mindview.util.Print.print;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2/2/12
 * Time: 9:15 PM
 * To change this template use File | Settings | File Templates.
 */
class StringFirst40 implements  Comparable<StringFirst40>{
    protected String first;
    protected String second;

    public StringFirst40(String first, String second) {
        this.first = first;
        this.second = second;
    }

    public int compareTo(StringFirst40 another) {
        return new CompareToBuilder()
                .append(this.first, another.first)
                .toComparison();
    }
    public String toString() {
        return  " \"" + first + " : " + second + "\"";
    }

    static Comparator<StringFirst40>  secondComparator = new Comparator<StringFirst40>() {
        public int compare(StringFirst40 a, StringFirst40 b) {
            return new CompareToBuilder()
                    .append(a.second, b.second)
                    .toComparison();
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringFirst40 that = (StringFirst40) o;

        if (first != null ? !first.equals(that.first) : that.first != null) return false;
        if (second != null ? !second.equals(that.second) : that.second != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }
}
public class Exercise40 {
    public static void printArray(StringFirst40[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for(int i = 0; i < array.length; i++) {
            sb.append(array[i] + ", ");
        }
        sb.setCharAt(sb.lastIndexOf(","), ']');
        print(sb.toString());
    }
    public static void main(String[] args) {
        Generator<String> gen = new RandomGenerator.String(20);
        int count = 1000;
        StringFirst40[] firstArray = new StringFirst40[count];
        List<StringFirst40> firstList = new ArrayList<StringFirst40>(count);

        for(int i = 0; i < count; i++) {
            firstArray[i] = new StringFirst40(gen.next(), gen.next());

        }
        firstList.addAll(Arrays.asList(firstArray));

        printArray(firstArray);
        print(firstList);

        Arrays.sort(firstArray);
        Collections.sort(firstList);

        printArray(firstArray);
        print(firstList);

        Arrays.sort(firstArray, StringFirst40.secondComparator);
        Collections.sort(firstList, StringFirst40.secondComparator);

        printArray(firstArray);
        print(firstList);
        
        Random rand = new Random();
        int index = -1;

        do{
            index = rand.nextInt(count);
        } while( index <= 0);

        StringFirst40 randItem = firstArray[index];

        int found1 = Arrays.binarySearch(firstArray, randItem, StringFirst40.secondComparator);
        int found2 = Collections.binarySearch(firstList, randItem, StringFirst40.secondComparator);
        print(firstArray[found1].equals(firstArray[index]));
        print(firstList.get(found2).equals(firstList.get(index)));

    }
}
