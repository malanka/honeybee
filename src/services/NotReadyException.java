package services;

public class NotReadyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1879298330594963529L;

	public NotReadyException() {
		super("Object is not ready to be used");
	}

	public NotReadyException(String message) {
		super(message);
	}

	public NotReadyException(Throwable cause) {
		super(cause);
	}

	public NotReadyException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotReadyException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
