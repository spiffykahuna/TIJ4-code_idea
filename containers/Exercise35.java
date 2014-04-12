package containers;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 2/1/12
 * Time: 10:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class Exercise35 {
    public static void main(String[] args) {
        Tester.defaultParams = TestParam.array(
                10, 5000, 100, 5000, 1000, 100, 10000, 20);
        Tester.run(new SlowMap<Integer, Integer>(), MapPerformance.tests);
    }
}
