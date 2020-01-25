package exceptions;

public class IllegalMoveException extends Exception {

	public IllegalMoveException(String arg0) {
		super("ILLEGAL ime " + arg0);
	}
}
