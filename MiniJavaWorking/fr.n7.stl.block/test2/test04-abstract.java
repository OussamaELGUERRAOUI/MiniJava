abstract class A {
	
	public A() {
		print 1;
	}
	
	//public abstract int get();
}

class B implements A {
	public int get() {
		print 1;
	}
}

public class Main {

	public static void main(String args[]) {
		print 1;
    }
}