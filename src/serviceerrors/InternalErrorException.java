package serviceerrors;

public class InternalErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -130618953831112769L;

	public InternalErrorException() {
		super("Unknown Internal Server error");
	}

	public InternalErrorException(String message) {
		super(message);
	}

	public InternalErrorException(Throwable cause) {
		super(cause);
	}

	public InternalErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public InternalErrorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
