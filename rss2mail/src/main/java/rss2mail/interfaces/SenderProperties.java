package rss2mail.interfaces;

import com.google.gson.JsonObject;

public interface SenderProperties  {
	public String getUserGroupsClass();
	public String getUserGroupsId();
	public String getTitle();
	public JsonObject toJson();
	public String toString();
}
