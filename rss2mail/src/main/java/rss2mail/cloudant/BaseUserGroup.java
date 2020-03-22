package rss2mail.cloudant;

import java.util.Vector;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cloudant.utilities.CloudantElement;
import rss2mail.interfaces.UserGroup;

public class BaseUserGroup implements CloudantElement, UserGroup {

	private String name;
	private Vector<BaseUser> users;
	private String language;
	private String timezone;
	private String dateformat;

	
	public Vector<BaseUser> getUsers() {
		return users;
	}
	
	public void setUsers(Vector<BaseUser> users) {
		this.users = users;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
		
	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", getName());		
		if(users!=null) jsonObject.add("users", toJsonArray(users));				
		return jsonObject;
	}
	
	public static JsonArray toJsonArray(Vector<?> objects) {
		JsonArray jsonArray = new JsonArray();
		for(int i=0; i<objects.size(); i++) {
			Object object = objects.get(i);
			if(object.getClass().isInstance("String")) {
				String string = (String)object;
				jsonArray.add(string);
			} else {
				CloudantElement element = (CloudantElement)object;
				jsonArray.add(element.toJson());
			}
		}
		return jsonArray;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getDateformat() {
		return dateformat;
	}

	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}
}
