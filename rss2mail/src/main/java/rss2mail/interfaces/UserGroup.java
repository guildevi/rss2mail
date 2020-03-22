package rss2mail.interfaces;

import java.util.Vector;

import com.google.gson.JsonObject;
import rss2mail.cloudant.BaseUser;

public interface UserGroup {
	public Vector<BaseUser> getUsers() ;
	public String getName() ;
	public String getLanguage() ;
	public String getTimezone() ;
	public String getDateformat() ;
	public JsonObject toJson();
}
