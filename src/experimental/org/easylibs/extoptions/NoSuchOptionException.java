package org.easylibs.extoptions;

public class NoSuchOptionException extends Exception {

	private static final long serialVersionUID = 1202103217250030204L;

	public NoSuchOptionException() {
	}

	public NoSuchOptionException(String message) {
		super(message);
	}

	public NoSuchOptionException(Throwable cause) {
		super(cause);
	}

	public NoSuchOptionException(String message, Throwable cause) {
		super(message, cause);
	}

}
