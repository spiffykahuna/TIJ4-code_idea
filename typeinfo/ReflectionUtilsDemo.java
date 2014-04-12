package typeinfo;

import java.util.Collection;
import java.util.Set;

/**
 * Alex Tracer (c) 2009
 */
public class ReflectionUtilsDemo {

    static class A<K, L> {
        // String, Integer
    }

    static class B<P, Q, R extends Collection> extends A<Q, P> {
        // Integer, String, Set
    }

    static class C<X extends Comparable<String>, Y, Z> extends B<Z, X, Set<Long>> {
        // String, Double, Integer
    }

    static class D<M, N extends Comparable<Double>> extends C<String, N, M> implements H<N, M> {
        // Integer, Double
    }

    static class E extends D<Integer, Double> {
        //
    }

    static class F<T, S> extends E {
        // Byte, Long
    }

    static class G extends F<Byte, Long> {
        //
    }

    static interface H<H1, H2> extends L<H2> {

    }

    static interface L<L1> {

    }

    static int getTypeParameterCount(Class c) {
        return c.getTypeParameters().length;
    }

    static void printArguments(Object object, Class c) {
        System.out.println("Parameters for " + c.getSimpleName() + ":");
        for (int i = 0; i < getTypeParameterCount(c); i++) {
            try {
                Class foundClass = ReflectionUtils.getGenericParameterClass(object.getClass(), c, i);
                System.out.println(i + " " + foundClass.getName());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        A object = new E();

        printArguments(object, A.class);
        printArguments(object, B.class);
        printArguments(object, C.class);
        printArguments(object, D.class);
        printArguments(object, E.class);

        printArguments(object, H.class);
        printArguments(object, L.class);

    }


}