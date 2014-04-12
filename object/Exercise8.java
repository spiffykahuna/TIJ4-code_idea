import java.util.ArrayList;


public class Exercise8 {
		
	public static void main(String[] args) {
	    System.out.println("Starting...");
	    
	    ArrayList<StaticTest> staticMembers = new ArrayList<StaticTest>();
	    for(int i = 0; i< 100; i++) {
	    	StaticTest test = new StaticTest();	    	
	    	staticMembers.add(test);
	    }
	    for(int i = 0; i< 100; i++) {
	    	int item = staticMembers.get(i).i;
	    	System.out.println(item);
	    	
	    }
	    
		StaticTest.i = 20;
		
	    for(int i = 0; i< 100; i++) {
	    	int item = staticMembers.get(i).i;
	    	System.out.println(item);	    	
	    }
	    System.out.println("Finished...");
	  }
}
