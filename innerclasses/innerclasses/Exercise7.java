package innerclasses;

public class Exercise7 {
	private String privField;
	
	private void printPrivField() {
		System.out.println(privField);
	}
	class Inner {
		public void modifyAndCall() {
			privField = "modified private field";
			printPrivField();
		}
	}
	
	private  void callInner() {
		Inner in1 = new Inner();
		in1.modifyAndCall();
		System.out.println("Modif outer field: " + privField);
	}
	public static void main(String[] args) {
		Exercise7 ex7 =  new Exercise7();
		ex7.callInner();
	}
}
