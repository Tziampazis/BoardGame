package exceptions;

public class IllegalCommandException extends Exception {

	public IllegalCommandException(String arg0) {
		super("ILLEGAL ice" + arg0);
	}

}
