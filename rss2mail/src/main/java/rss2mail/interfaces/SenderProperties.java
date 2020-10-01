package rss2mail.interfaces;

import com.google.gson.JsonObject;

public interface SenderProperties  {
	public String getServerClass();
	public String getServerDocId();
	public String getUserGroupsClass();
	public String getUserGroupsDocId();
	public String getTitle();
	public JsonObject toJson();
	public String toString();
}
