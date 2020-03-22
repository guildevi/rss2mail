package rss2mail.exceptions;

public class InvalidIncludeValueException extends RssException {

	public InvalidIncludeValueException() {
	}

	public InvalidIncludeValueException(String message) {
		super(message);
	}

	public InvalidIncludeValueException(Throwable cause) {
		super(cause);
	}

	public InvalidIncludeValueException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidIncludeValueException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
