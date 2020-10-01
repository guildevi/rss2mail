package rss2mail.interfaces;

import com.google.gson.JsonObject;

public interface SaxHandlerProperties  {
	public String getUrl();
	public String getItemClass();
	public String getPubDateFormat();
	public String getStatusClass();
	public String getStatusDocId();
	public JsonObject toJson();
	public String toString();
}
