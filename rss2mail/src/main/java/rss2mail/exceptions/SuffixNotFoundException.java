package rss2mail.exceptions;

public class SuffixNotFoundException extends RssException {

	public SuffixNotFoundException() {
	}

	public SuffixNotFoundException(String message) {
		super(message);
	}

	public SuffixNotFoundException(Throwable cause) {
		super(cause);
	}

	public SuffixNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public SuffixNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
