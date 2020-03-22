package rss2mail.interfaces;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonObject;

public interface RssItem {
	
	public String getTitle();
	public String getGuid();
	public Date getPubDate();
	public String getLink();
	public String getDescription();
	public SimpleDateFormat getPubDateFormat();
	
	public void appendToTitle(String title);
	public void appendToGuid(String guid);
	public void appendToPubDate(String pubDate);
	public void appendToLink(String link);
	public void appendToDescription(String description);
	
	public void setTitle(String title);
	public void setGuid(String guid);
	public void setPubDate(String pubDate);
	public void setLink(String link);
	public void setDescription(String description);
	public void setPubDateFormat(SimpleDateFormat format);
	
	public void setPubDate();
	public void setPubDate(Date pubDate);
	
	public String getMailSubject();
	public void init();
	
	public JsonObject toJson();
	public String toString();
}
