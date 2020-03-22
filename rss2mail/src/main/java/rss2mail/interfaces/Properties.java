package rss2mail.interfaces;

public interface Properties extends SaxHandlerProperties, SenderProperties {
	public String getSaxHandlerClass();
	public String getSenderClass();
}
