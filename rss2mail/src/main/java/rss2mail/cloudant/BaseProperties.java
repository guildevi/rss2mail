package rss2mail.cloudant;

import com.google.gson.JsonObject;

import cloudant.utilities.CloudantObject;
import rss2mail.interfaces.SaxHandlerProperties;
import rss2mail.interfaces.SenderProperties;

public class BaseProperties extends CloudantObject implements SaxHandlerProperties, SenderProperties {
	
	public BaseProperties(String _id) {
		super(_id);
	}

	private String url = null;
	private String title = null;
	private String senderClass = null;
	private String handlerClass = null;
	private String itemClass = null;
	private String statusClass = null;
	private String statusId = null;
	private String userGroupsClass = null;
	private String userGroupsId = null;
	private String pubDateFormat = null;
	//private int max = 10;
	//private int wait = 60;	
	//private RssStatusInterface rssStatus = null;
	
	public JsonObject toJson() {
		JsonObject jsonObject = super.toJson();
		jsonObject.addProperty("url", getUrl());
		jsonObject.addProperty("title", getTitle());
		jsonObject.addProperty("senderClass", getSenderClass());
		jsonObject.addProperty("handlerClass", getHandlerClass());
		jsonObject.addProperty("itemClass", getItemClass());
		jsonObject.addProperty("statusClass", getStatusClass());
		jsonObject.addProperty("groupsClass", getUserGroupsClass());
		jsonObject.addProperty("statusId", getStatusId());
		jsonObject.addProperty("groupsClass", getUserGroupsId());
		return jsonObject;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSenderClass() {
		return senderClass;
	}

	public void setSenderClass(String senderClass) {
		this.senderClass = senderClass;
	}

	public String getHandlerClass() {
		return handlerClass;
	}

	public void setHandlerClass(String handlerClass) {
		this.handlerClass = handlerClass;
	}

	public String getItemClass() {
		return itemClass;
	}

	public void setItem(String itemClass) {
		this.itemClass = itemClass;
	}

	public String getPubDateFormat() {
		return pubDateFormat;
	}

	public void setPubDateFormat(String pubDateFormat) {
		this.pubDateFormat = pubDateFormat;
	}
	
	public String getStatusClass() {
		return statusClass;
	}
	
	public void setStatusClass(String statusClass) {
		this.statusClass = statusClass;
	}
	
	public String getStatusId() {
		return statusId;
	}
	
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	
	public String getUserGroupsClass() {
		return userGroupsClass;
	}
	
	public void setUserGroupsClass(String userGroupsClass) {
		this.userGroupsClass = userGroupsClass;
	}
	
	public String getUserGroupsId() {
		return userGroupsId;
	}
	
	public void setUserGroupsId(String userGroupsId) {
		this.userGroupsId = userGroupsId;
	}
		
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
