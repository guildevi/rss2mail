package rss2mail.interfaces;

import java.util.List;

public interface Sender {
	public void send(List<RssItem> rssItems) throws Exception;
	public void send(List<RssItem> rssItems, UserGroups userGroups) throws Exception;
	//public String getSubject(BaseUserGroup userGroup, RssItem item) throws RssException;
	//public boolean filter(BaseUserGroup userGrouo, RssItem item) throws RssException;
	//public String getContent(BaseUserGroup userGroup, RssItem item) throws RssException;
}
