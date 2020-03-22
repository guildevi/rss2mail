package rss2mail.ibmcloud.pup.cloudant;

import java.util.Iterator;
import java.util.Vector;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cloudant.utilities.CloudantObject;
import rss2mail.interfaces.UserGroup;
import rss2mail.interfaces.UserGroups;

public class PupUserGroups extends CloudantObject implements UserGroups {

	private Vector<PupUserGroup> userGroups ;
		
	public PupUserGroups(String _id) {
		super(_id);
	}

	public Vector<PupUserGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(Vector<PupUserGroup> userGroups) {
		this.userGroups = userGroups;
	}

	public JsonObject toJson() {
		JsonObject jsonObject = super.toJson();
		JsonArray jsonArray = new JsonArray();
		for(PupUserGroup userGroup : userGroups) {
			jsonArray.add(userGroup.toJson());
		}
		jsonObject.add("usergroups", jsonArray);
		return jsonObject;
	}
	
	public Iterator<UserGroup> iterator() {
		Vector<UserGroup> userGroups = new Vector<UserGroup>();
		for(PupUserGroup userGroup : getUserGroups()) {
			userGroups.add(userGroup);
		}		
		return (Iterator<UserGroup>)userGroups.iterator();
	}
}
