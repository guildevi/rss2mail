package rss2mail.cloudant;

import com.google.gson.JsonObject;

import cloudant.utilities.CloudantObject;
import rss2mail.interfaces.SaxHandlerProperties;
import rss2mail.interfaces.SenderProperties;

public class BaseProperties extends CloudantObject 
	implements SaxHandlerProperties, SenderProperties {
	
	public BaseProperties(String _id) {
		super(_id);
	}

	private String url = null;
	private String title = null;
	private String senderClass = null;
	private String handlerClass = null;
	private String itemClass = null;
	
	private String statusClass = null;
	private String statusDocId = null;
	
	private String serverClass = null;
	private String serverDocId = null;
	
	private String userGroupsClass = null;
	private String userGroupsDocId = null;
	
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
		
		
		jsonObject.addProperty("serverClass", getStatusClass());
		jsonObject.addProperty("serverDocId", getStatusDocId());
		
		jsonObject.addProperty("statusClass", getStatusClass());
		jsonObject.addProperty("statusDocId", getStatusDocId());
		
		jsonObject.addProperty("userGroupsClass", getUserGroupsClass());
		jsonObject.addProperty("userGroupsDocId", getUserGroupsDocId());
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
	
	public String getStatusDocId() {
		return statusDocId;
	}
	
	public void setStatusDocId(String statusId) {
		this.statusDocId = statusId;
	}
	
	public String getUserGroupsClass() {
		return userGroupsClass;
	}
	
	public void setUserGroupsClass(String userGroupsClass) {
		this.userGroupsClass = userGroupsClass;
	}
	
	public String getUserGroupsDocId() {
		return userGroupsDocId;
	}
	
	public void setUserGroupsDocId(String userGroupsId) {
		this.userGroupsDocId = userGroupsId;
	}
		
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getServerClass() {
		return serverClass;
	}

	public void setServerClass(String serverClass) {
		this.serverClass = serverClass;
	}

	public String getServerDocId() {
		return serverDocId;
	}

	public void setServerDocId(String serverDocId) {
		this.serverDocId = serverDocId;
	}
}
