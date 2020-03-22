package rss2mail.exceptions;

public class InvalidSourceDateFormatException extends RssException {

	public InvalidSourceDateFormatException() {
	}

	public InvalidSourceDateFormatException(String message) {
		super(message);
	}

	public InvalidSourceDateFormatException(Throwable cause) {
		super(cause);
	}

	public InvalidSourceDateFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidSourceDateFormatException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
