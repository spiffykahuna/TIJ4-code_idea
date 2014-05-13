package annotations.exercises;

@ExtractInterface2("IDivider2")
public class Divider2 {
    public double divide(double divident, double divisor) {
        return divident/divisor;
    }
    public static void main(String[] args) {
        Divider2 m = new Divider2();
        System.out.println("11*16 = " + m.divide(15.0, 3.0));
    }
}
