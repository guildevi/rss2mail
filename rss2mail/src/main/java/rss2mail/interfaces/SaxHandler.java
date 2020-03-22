package rss2mail.interfaces;

import java.util.List;

public interface SaxHandler {
	public List<RssItem> parse() throws Exception;
	public void commit() throws Exception;
}
