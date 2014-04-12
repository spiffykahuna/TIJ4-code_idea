package containers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import static net.mindview.util.Print.print;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2/1/12
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */
class Ex30Tests {
	static Random rand = new Random();
	public static ArrayList<Integer> createFullArrayList(int size) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < size; i++) list.add(rand.nextInt(size));
		return list;
	}
	public static LinkedList<Integer> createFullLinkedList(int size) {
		LinkedList<Integer> list = new LinkedList<Integer>();
		for(int i = 0; i < size; i++) list.add(rand.nextInt(size));
		return list;
	}
	public static long meanSortTimeLinkedList(int size, int reps) {
		long time = 0;
		for(int i = 0; i < reps; i++) {
			LinkedList<Integer> lList = createFullLinkedList(size);
			long sortTime = timedLinkedListSort(lList);
			time += sortTime;
		}
		return time/reps;
	}
	public static long meanSortTimeArrayList(int size, int reps) {
		long time = 0;
		for(int i = 0; i < reps; i++) {
			ArrayList<Integer> aList = createFullArrayList(size);
			long sortTime = timedArrayListSort(aList);
			time += sortTime;
		}
		return time/reps;
	}
	public static long timedArrayListSort(ArrayList<Integer> list) {
			long start = System.nanoTime();
			Collections.sort(list);
			long stop = System.nanoTime();
			return stop - start;
	}
	public static long timedLinkedListSort(LinkedList<Integer> list) {
			long start = System.nanoTime();
			Collections.sort(list);
			long stop = System.nanoTime();
			return stop - start;
	}
	public static void main(String[] args) {
		print("Collections.sort() times (nanosecs):");
		print();
		print("Means of 10000 sorts:");
		print();
		print("Lists of 10 elements:");
		print("LinkedList: " + meanSortTimeLinkedList(10,10000));
		print("ArrayList:  " + meanSortTimeArrayList(10,10000));
		print();
		print("Lists of 100 elements:");
		print("LinkedList: " + meanSortTimeLinkedList(100,10000));
		print("ArrayList:  " + meanSortTimeArrayList(100,10000));
		print();
		print("Means of 1000 sorts:");
		print();
		print("Lists of 1000 elements:");
		print("LinkedList: " + meanSortTimeLinkedList(1000,1000));
		print("ArrayList:  " + meanSortTimeArrayList(1000,1000));
	}
}
public class Exercise30 {
    public static void main(String[] args) {
        Ex30Tests.main(null);
    }
}
