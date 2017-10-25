package cz.lukasburda.datacrypting;

public class SecurityException extends Exception {

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
