package rss2mail.exceptions;

public class RssException extends Exception {

	public static final long serialVersionUID = 0;

	public RssException() {
	}

	public RssException(String message) {
		super(message);
	}

	public RssException(Throwable cause) {
		super(cause);
	}

	public RssException(String message, Throwable cause) {
		super(message, cause);
	}

	public RssException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
