package containers;

import net.mindview.util.Generator;
import net.mindview.util.RandomGenerator;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/25/12
 * Time: 5:21 PM
 * To change this template use File | Settings | File Templates.
 */

class MySortedSet<T> extends LinkedList<T> implements SortedSet<T> {

    public  MySortedSet(Collection<T> coll) {
        super(coll);
        sortMe();
    }

    public  MySortedSet() { super();}

    public Comparator<? super T> comparator() {

        return new Comparator<T>() {
            public int compare(T t, T t1) {
                //return (o1.j < o2.j ? -1 : (o1.j == o2.j ? 0 : 1));
                return (t.hashCode() < t1.hashCode() ? -1 :
                        (t.hashCode() == t1.hashCode() ? 0 : 1));
            }
        };
    }

    public SortedSet<T> subSet(T t, T t1) {
//        int firstIndex = this.indexOf(t);
//        int secondIndex = this.indexOf(t1);
//        return new MySortedSet<T>(this.subList(firstIndex,secondIndex));
        LinkedList<T> intersect = new LinkedList<T>(headSet(t1));
        intersect.retainAll(tailSet(t));
        return new MySortedSet<T>(intersect);
    }

    public SortedSet<T> headSet(T t) {
        Comparator<? super T> comp = comparator();
        if( comp.compare(t, this.first()) < 0 )
            return new MySortedSet<T>();
        if( comp.compare(t, this.last()) >= 0 )
            return this;
        Iterator<T> it = this.iterator();
        MySortedSet<T> result = new MySortedSet();

        while(it.hasNext()) {
            T value = it.next();
            if( comp.compare(value, t) < 0)
                result.add(value);
        }

        return result;
    }

    public SortedSet<T> tailSet(T t) {
        Comparator<? super T> comp = comparator();
        if( comp.compare(t, this.first()) < 0 )
            return this;
        if( comp.compare(t, this.last()) >= 0 )
            return new MySortedSet<T>();
        Iterator<T> it = this.iterator();
        MySortedSet<T> result = new MySortedSet();

        while(it.hasNext()) {
            T value = it.next();
            if( comp.compare(value, t) >= 0)
                result.add(value);
        }

        return result;
    }

    public T first() {
        if(this.size() == 0) throw new NoSuchElementException();
        return this.getFirst();
    }

    public T last() {
        if(this.size() == 0) throw new NoSuchElementException();
        return this.getLast();
    }

    public boolean add(T e) {
		if(!this.contains(e)) {
            super.add(e);
			sortMe();
			return true;
		}
		return false;
	}

    private void sortMe() {
       Collections.sort(this, comparator());
    }
}


public class Exercise10 {
    public static void main(String[] args) {
        SortedSet<Integer>  mySet = new MySortedSet<Integer>();
        SortedSet<Integer>  rightSet = new TreeSet<Integer>();
        List<Integer>  insertionOrder = new LinkedList<Integer>();
        Generator<Integer> gen = new RandomGenerator.Integer(25);

        for(int i = 0; i < 25; i++) {
            int value = gen.next();
            mySet.add(value);
            rightSet.add(value);
            insertionOrder.add(value);
        }

        System.out.println(insertionOrder);
        System.out.println(mySet);
        System.out.println(rightSet);
        System.out.println(mySet.equals(rightSet));
        System.out.println(mySet == rightSet);
        System.out.println(mySet.first() == rightSet.first());
        System.out.println(mySet.last() == rightSet.last());

        System.out.println(mySet.subSet(1, 10));
        System.out.println(rightSet.subSet(1, 10));

        System.out.println(mySet.headSet(10));
        System.out.println(rightSet.headSet(10));

        System.out.println(mySet.tailSet(0));
        System.out.println(rightSet.tailSet(0));



    }
}
