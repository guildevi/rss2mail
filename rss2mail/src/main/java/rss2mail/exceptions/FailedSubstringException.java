package rss2mail.exceptions;

public class FailedSubstringException extends RssException {

	public FailedSubstringException() {
	}

	public FailedSubstringException(String message) {
		super(message);
	}

	public FailedSubstringException(Throwable cause) {
		super(cause);
	}

	public FailedSubstringException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedSubstringException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
