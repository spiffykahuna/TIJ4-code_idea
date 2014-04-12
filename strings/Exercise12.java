
//: strings/Groups.java
import java.util.HashSet;
import java.util.Set;
import java.util.regex.*;
import static net.mindview.util.Print.*;

/* Output:
[the slithy toves][the][slithy toves][slithy][toves]
[in the wabe.][in][the wabe.][the][wabe.]
[were the borogoves,][were][the borogoves,][the][borogoves,]
[mome raths outgrabe.][mome][raths outgrabe.][raths][outgrabe.]
[Jabberwock, my son,][Jabberwock,][my son,][my][son,]
[claws that catch.][claws][that catch.][that][catch.]
[bird, and shun][bird,][and shun][and][shun]
[The frumious Bandersnatch.][The][frumious Bandersnatch.][frumious][Bandersnatch.]
*///:~

public class Exercise12 {
	static public final String POEM =
	    "Twas brillig, and the slithy toves\n" +
	    "Did gyre and gimble in the wabe.\n" +
	    "All mimsy were the borogoves,\n" +
	    "And the mome raths outgrabe.\n\n" +
	    "Beware the Jabberwock, my son,\n" +
	    "The jaws that bite, the claws that catch.\n" +
	    "Beware the Jubjub bird, and shun\n" +
	    "The frumious Bandersnatch.";
	  public static void main(String[] args) {
	    Matcher m =
	      Pattern.compile("\\w+")
	        .matcher(POEM);
	    int total = 0;
	    Set<String> uniqWords = new HashSet<String>();
	    while(m.find()) {
	    	
	      for(int j = 0; j <= m.groupCount(); j++) {
	    	  if(!m.group(j).matches("[A-Z].*")) {
	    		  printnb("[" + m.group(j) + "]"); 
	    		  uniqWords.add(m.group(j));
	    	  }  
	      }	    		  
	      print();
	    }
	    print(uniqWords);
	    print("Total unique words: " + uniqWords.size());
	  }
}
