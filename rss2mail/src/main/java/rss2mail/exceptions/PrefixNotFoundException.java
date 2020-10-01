package rss2mail.exceptions;

public class PrefixNotFoundException extends RssException {

	public static final long serialVersionUID = 0;

	public PrefixNotFoundException() {
	}

	public PrefixNotFoundException(String message) {
		super(message);
	}

	public PrefixNotFoundException(Throwable cause) {
		super(cause);
	}

	public PrefixNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrefixNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
