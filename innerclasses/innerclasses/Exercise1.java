package innerclasses;

public class Exercise1 {
	private String strMember;
	
	public Exercise1(String str) {
		strMember = str;
	}
	class Inner {
		public String toString() {
			return strMember;
		}
		
	}
	Inner getInner() {
		return new Inner();
	}
	public static void main(String[] args) {
//		Exercise1 ex1 = new Exercise1();
//		Exercise1.Inner in1 = ex1.getInner();
//		//System.out.println("hehe");
		Exercise1 ex3 = new Exercise1("test Prog");
		Inner in2 = ex3.getInner();
		System.out.println(in2);
	}
}
