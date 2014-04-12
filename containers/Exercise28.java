package containers;

import net.mindview.util.*;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

import static net.mindview.util.Print.print;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/29/12
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
class TwoTuple28<A,B> extends TwoTuple<A,B>  implements Comparable {

    public TwoTuple28(A a, B b) {
        super(a, b);
    }

    public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	public int compareTo(Object obj) {
        return CompareToBuilder.reflectionCompare(this, obj);
	}
  	public String toString() {
  	  //return "(" + first + ", " + second + ")";
          return "(" + ToStringBuilder.reflectionToString(this) + ")";
  	}
}

class ThreeTuple28<A,B,C> extends TwoTuple28<A,B>
implements Comparable {
  	public final C third;
  	public ThreeTuple28(A a, B b, C c) {
  		super(a, b);
    		third = c;
  	}
}

class FourTuple28<A,B,C,D> extends ThreeTuple28<A,B,C>
implements Comparable {
  	public final D fourth;
  	public FourTuple28(A a, B b, C c, D d) {
    		super(a, b, c);
    		fourth = d;
  	}

}

class FiveTuple28<A,B,C,D,E>
extends FourTuple28<A,B,C,D>
implements Comparable {
  	public final E fifth;
  	public FiveTuple28(A a, B b, C c, D d, E e) {
    		super(a, b, c, d);
    		fifth = e;
  	}
}

class Tuple28 {
  	public static <A,B> TwoTuple28<A,B> tuple(A a, B b) {
    		return new TwoTuple28<A,B>(a, b);
  	}
  	public static <A,B,C> ThreeTuple28<A,B,C>
  	tuple28(A a, B b, C c) {
    		return new ThreeTuple28<A,B,C>(a, b, c);
  	}
  	public static <A,B,C,D> FourTuple28<A,B,C,D>
  	tuple(A a, B b, C c, D d) {
    		return new FourTuple28<A,B,C,D>(a, b, c, d);
  	}
  	public static <A,B,C,D,E>
  	FiveTuple28<A,B,C,D,E> tuple(A a, B b, C c, D d, E e) {
    		return new FiveTuple28<A,B,C,D,E>(a, b, c, d, e);
  	}
}

public class Exercise28 {
    public static void main(String[] args) {
        Tuple28 t = new Tuple28();
		FiveTuple28 t1b = t.tuple(1,1,1,1,1);
		FiveTuple28 t2b = t.tuple(1,1,1,2,1);
		FiveTuple28 t3b = t.tuple(1,1,1,1,1);
		FiveTuple28 t4b = t.tuple(1,1,1,1,0);
		print(t1b.compareTo(t1b));
		print(t1b.compareTo(t2b));
		print(t1b.compareTo(t3b));
		print(t1b.compareTo(t4b));
		List<FiveTuple28> list =
			new ArrayList<FiveTuple28>(Arrays.asList(t1b, t2b, t3b, t4b));
		Set<FiveTuple28> s = new TreeSet<FiveTuple28>();
		s.addAll(list);
		print(s);

    }
}
