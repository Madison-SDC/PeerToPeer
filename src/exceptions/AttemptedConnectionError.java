package exceptions;

public class AttemptedConnectionError extends Exception {
	
	private static final long serialVersionUID = 123467;
	
	public AttemptedConnectionError() { super(); }
	public AttemptedConnectionError(String msg) { super(msg); }
}
