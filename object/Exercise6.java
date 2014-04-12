
public class Exercise6 {
	static int storage(String s) {
		return s.length() * 2;
	}
	
	public static void main(String[] args) {
	    System.out.println("Starting...");
	    
	    String myString = "This is a string";    
	    
	    System.out.println("String requires amount of bytes: " + 
	    		storage(myString));	   
	    
	    System.out.println("Finished...");
	  }
}
