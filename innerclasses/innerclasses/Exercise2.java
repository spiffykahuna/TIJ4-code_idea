package innerclasses;


public class Exercise2 {
	String strMember =  "this is the string";
	public String toString() {
		return strMember;
	}
	public static void main(String[] args) {
		Sequence seq = new Sequence(20);
		for(int i = 0;i < 20; i++) {
			seq.add(new Exercise2());
		}
		Selector selector = seq.selector();
		while(!selector.end()) {
			System.out.println(selector.current());
			selector.next();
		}
	}
}
