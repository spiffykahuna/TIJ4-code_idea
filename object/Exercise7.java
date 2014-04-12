
public class Exercise7 {
		
	public static void main(String[] args) {
	    System.out.println("Starting...");
	    
	    for(int i = 0; i< 100; i++) {
	    	Incremetable.increment();
	    	System.out.println("i = " + StaticTest.i);
	    }	    
	    System.out.println("Finished...");
	  }
}
