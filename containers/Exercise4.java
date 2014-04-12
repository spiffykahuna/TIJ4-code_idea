package containers;

import net.mindview.util.TextFile;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 1/25/12
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class Exercise4 {
	static Collection<String> CollectFromText(String fileName) {
		String[] sa = TextFile.read(fileName).split(" ");
		return Arrays.asList(sa);
	}
	static Collection<String> CollectFromText2(String fileName) {
		String[] sa = TextFile.read(fileName).split(" ");
		return new TreeSet<String>(new TextFile(fileName, "\\W+"));
	}
	public static void main(String[] args) {
		System.out.println(CollectFromText("containers/Exercise4.java"));
		System.out.println(CollectFromText2("containers/Exercise4.java"));
	}
}

