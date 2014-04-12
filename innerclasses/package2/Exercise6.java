package package2;

import package1.MyInterface;

public class Exercise6 {
	protected class PMyInterface implements MyInterface {
		public PMyInterface() {};
		private String bu = "pipec bu";
		public String getStringLabel() {
			return bu;
		}
	}
}
