package rss2mail.cloudant;

import java.util.Iterator;
import java.util.Vector;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cloudant.utilities.CloudantObject;
import rss2mail.interfaces.UserGroup;
import rss2mail.interfaces.UserGroups;

public class BaseUserGroups extends CloudantObject implements UserGroups {

	private Vector<BaseUserGroup> userGroups ;
		
	public BaseUserGroups(String _id) {
		super(_id);
	}

	public Vector<BaseUserGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(Vector<BaseUserGroup> userGroups) {
		this.userGroups = userGroups;
	}

	public JsonObject toJson() {
		JsonObject jsonObject = super.toJson();
		JsonArray jsonArray = new JsonArray();
		for(BaseUserGroup userGroup : userGroups) {
			jsonArray.add(userGroup.toJson());
		}
		jsonObject.add("usergroups", jsonArray);
		return jsonObject;
	}
	
	public Iterator<UserGroup> iterator() {
		Vector<UserGroup> userGroups = new Vector<UserGroup>();
		for(BaseUserGroup userGroup : getUserGroups()) {
			userGroups.add(userGroup);
		}		
		return (Iterator<UserGroup>)userGroups.iterator();
	}
}
