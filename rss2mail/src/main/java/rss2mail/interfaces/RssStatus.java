package rss2mail.interfaces;

import java.util.Date;

import com.google.gson.JsonObject;

public interface RssStatus {
	public int getSize();
	public Date getTimestamp();
	public void setSize(int size);
	public void setTimestamp(Date timestamap);
	//public void update() throws CloudantException;
	public JsonObject toJson();
	public String toString();
}
