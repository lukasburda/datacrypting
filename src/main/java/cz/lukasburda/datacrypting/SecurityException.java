package cz.lukasburda.datacrypting;

public class SecurityException extends Exception {

	private static final long serialVersionUID = 1L;

	public SecurityException(Throwable th) {
		super(th);
	}

	public SecurityException(String message) {
		super(message);
	}

	public SecurityException(String message, Throwable th) {
		super(message, th);
	}
}
