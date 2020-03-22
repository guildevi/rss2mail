package rss2mail.ibmcloud.pup.cloudant;

import java.util.Date;

import com.google.gson.JsonObject;

import rss2mail.cloudant.BaseRssStatus;

public class PupRssItemStatus {

	protected String guid;
	protected String status;
	protected Date date;
	
	public PupRssItemStatus() {
	}

	public PupRssItemStatus(String guid, String Status) {
		setGuid(guid);
		setStatus(Status,new Date(System.currentTimeMillis()));
	}

	public PupRssItemStatus(String guid, String Status, Date date) {
		setGuid(guid);
		setStatus(Status,date);
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setStatus(String status, Date date) {
		setDate(date);
		setStatus(status);
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{").append(getGuid());
		buffer.append(", ").append(getStatus());
		buffer.append(", ").append(getDate().toString());
		buffer.append('}');
		return buffer.toString();
	}
	
	public JsonObject toJsonObject() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("guid", getGuid());
		jsonObject.addProperty("status", getStatus());
		jsonObject.addProperty("timestamp", BaseRssStatus.defaultDateFormat.format(getDate()));
		return jsonObject;
	}
}
