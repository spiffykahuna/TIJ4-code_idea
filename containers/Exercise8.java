package containers;



/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/25/12
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */

class SLink<T> {
        public T value;
        public SLink<T> next;
        public SLink(T value, SLink<T> next) {
            this.value = value;
            this.next = next;
        }

    }
    class SList<T>{
        public SLink<T> first = new SLink<T>(null, null);

        public SListIterator<T> iterator() {
            return new SListIterator<T>(first);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            SListIterator<T> slit = iterator();


            sb.append("SList : [ ");
            while(slit.hasNext()) {
                sb.append(slit.next() + (slit.hasNext() ? " ": ""));
            }
            sb.append("]");
            return sb.toString();
        }


    }

    class SListIterator<T> {

        private SLink<T>  currentElement;
        private int size;
        public SListIterator(SLink link) {
            this.currentElement = link;
            this.size = 0;
        }

        public boolean hasNext(){
            return currentElement.next != null;
        }
        public T next(){
            currentElement = currentElement.next;
            return currentElement.value;
        }

        public void add(T object) {
            currentElement.next = new SLink<T>(object, currentElement.next);
            currentElement = currentElement.next;
        }
        public void remove() {
            currentElement.next = currentElement.next.next;
        }

    }

public class Exercise8 {

    public static void main(String[] args) {
        SList<Integer> testList = new SList<Integer>();
        SListIterator<Integer> slit = testList.iterator();

        print(testList);
        slit.add(1);
        slit.add(2);
        slit.add(3);

        print(testList);
        for(int i = 0; i < 10; i++)
            slit.add(i);

        print(testList);

        slit = testList.iterator();
        while(slit.hasNext())
            System.out.print(slit.next()  + " ");
        print("");
        slit = testList.iterator();
        while(slit.hasNext()){
            slit.remove();
            print(testList);
        }


        SList<String> sl = new SList<String>();
		print(sl);
		SListIterator<String> slIter = sl.iterator();
		print("adding \"hi\"");
		slIter.add("hi");
		print(sl);
		print("adding \"there\"");
		slIter.add("there");
		print(sl);
		print("adding \"sweetie\"");
		slIter.add("sweetie");
		print(sl);
		print("adding \"pie\"");
		slIter.add("pie");
		print(sl);
		SListIterator<String> slIter2 = sl.iterator();
		print("removing \"hi\"");
		slIter2.remove();
		print(sl);
		print("adding \"hello\"");
		slIter2.add("hello");
		print(sl);
		print("removing \"there\"");
		slIter2.remove();
		print(sl);
		print("removing \"sweetie\"");
		slIter2.remove();
		print(sl);
		print("removing \"pie\"");
		slIter2.remove();
		print(sl);
		print("removing \"hello\"");
		SListIterator slIter3 = sl.iterator();
		slIter3.remove();
		print(sl);




    }

    public static void print(Object o) {
        System.out.println(o);
    }
}
