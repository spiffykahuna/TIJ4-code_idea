
public class Exercise9 {
		
	public static void main(String[] args) {
	    System.out.println("Starting...");
	    
	    boolean b1 = true;
	    Boolean b2 = b1;
	    Boolean b3 = new Boolean(false);
	    boolean b4 = b3;
	    
	    System.out.println("Boolean: " +  b1 + " " + b2 + " " + b3 + " " + b4);
	    
	    char c1 = 'b';
	    Character c2 = c1;
	    Character c3 = new Character('c');
	    char c4 = c3;
	    
	    System.out.println("Character: " +  c1 + " " + c2 + " " + c3 + " " + c4);
	    
	    byte bb1 = -120;
	    Byte bb2 = bb1;
	    Byte bb3 = new Byte("66");
	    byte bb4 = bb3;
	    
	    System.out.println("Byte: " +  bb1 + " " + bb2 + " " + bb3 + " " + bb4);
	    
	    short sh1 = -666;
	    Short sh2 = sh1;
	    Short sh3 = new Short("66");
	    short sh4 = sh3;
	    
	    System.out.println("Short: " +  sh1 + " " + sh2 + " " + sh3 + " " + sh4);
	    
	    int i1 = -55555;
	    Integer i2 = i1;
	    Integer i3 = new Integer("22222");
	    int i4 = i3;
	    
	    System.out.println("Integer: " +  i1 + " " + i2 + " " + i3 + " " + i4);
	    
	    long l1 = -1111111111111111111l;
	    Long l2 = l1;
	    Long l3 = new Long("444444444444444");
	    Long l4 = l3;
	    
	    System.out.println("Long: " +  l1 + " " + l2 + " " + l3 + " " + l4);
	    
	    float f1 = -31231.313131323232f;
	    Float f2 = f1;
	    Float f3 = new Float("222211.212112");
	    float f4 = f3;
	    
	    System.out.println("Float: " +  f1 + " " + f2 + " " + f3 + " " + f4);
	    
	    double d1 = -3434334.313131323232f;
	    Double d2 = d1;
	    Double d3 = new Double("56562323423.25666565");
	    double d4 = d3;
	    
	    System.out.println("Double: " +  d1 + " " + d2 + " " + d3 + " " + d4);
	    
	    System.out.println("Finished...");
	  }
}
