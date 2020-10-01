package rss2mail.cloudant;

import java.text.SimpleDateFormat;
//import java.text.SimpleDateFormat;
import java.util.Date;


import com.google.gson.JsonObject;

import cloudant.utilities.CloudantObject;
import rss2mail.interfaces.RssStatus;

public class BaseRssStatus extends CloudantObject implements RssStatus {
	
	public static SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

	private Date timestamp = null;
	private int size = 0;
	
	public BaseRssStatus() {
		super(BaseRssStatus.class.getCanonicalName());
	}

	public BaseRssStatus(String _id) {
		super(_id);
	}

	public Date getTimestamp() {
		return timestamp;
	}
	/*
	public void initDateTimestamp() throws CloudantException {
		if(getTimestamp()!=null) {
			try {
				this.dateTimestamp  = defaultDateFormat.parse(getTimestamp()); 
			} catch(Exception e) {
				Logger.error(this, "getTimestampAsDate", "Fail to parse "+getTimestamp()+": "+e.getMessage());
				throw new CloudantException(e);
			}
		}
	}
	*/
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	/*
	public void setDateTimestamp(Date date) {
		this.dateTimestamp = date;
		this.timestamp = defaultDateFormat.format(date); 
	}
	
	public Date getDateTimestamp() {
		return dateTimestamp;
	}
	*/
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public String toString() {
		return toJson().toString();
	}
	
	public JsonObject toJson() {
		JsonObject jsonRssStatus = super.toJson();
		if(getTimestamp()!=null)
			jsonRssStatus.addProperty("timestamp", defaultDateFormat.format(getTimestamp()));
		jsonRssStatus.addProperty("size", getSize());
		return jsonRssStatus;
	}
}
