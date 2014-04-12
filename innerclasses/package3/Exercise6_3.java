package package3;

import package1.MyInterface;
import package2.Exercise6;

public class Exercise6_3 extends Exercise6 {
	public MyInterface getInterfaceObject() {
		return new PMyInterface();
	}
	public static void main(String[] args) {
		Exercise6_3 ex = new Exercise6_3();
		MyInterface try1 = ex.getInterfaceObject();
		System.out.println(try1.getStringLabel());
	}

}
