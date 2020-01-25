package exceptions;

public class IllegalArgumentsException extends Exception {

	public IllegalArgumentsException (String arg0) {
		super("ILLEGAL iae " + arg0);
	}
}
