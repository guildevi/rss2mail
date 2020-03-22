package rss2mail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import com.google.gson.JsonObject;

import rss2mail.interfaces.RssItem;

/*
 * Stores an RSS feed
 */
public class BaseRssItem implements RssItem {
	
	public static Logger logger = 
			java.util.logging.Logger.getLogger(BaseRssItem.class.getCanonicalName());

	private SimpleDateFormat pubDateFormat ;
	
	public void setPubDateFormat(String pattern) {
		setPubDateFormat(new SimpleDateFormat(pattern));
	}
	
	public void setPubDateFormat(SimpleDateFormat format) {
		this.pubDateFormat = format; 
	}
	
	public SimpleDateFormat getPubDateFormat() {
		return pubDateFormat; 
	}
	
    private String title;
    private String link;
    private String guid;
    private String description;
    private String pubDateString;
    private Date pubDate;
    
    public BaseRssItem(Date pubDate, String title) {
        this.title = title;
        this.pubDate = pubDate;
    }
    
    public void init() {
    		// DO NOTHING
    }

    public BaseRssItem() {
    		super();
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getGuid() {
        return guid;
    }

    public String getDescription() {
        return description;
    }
    
    public Date getPubDate() {
        return pubDate;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPubDate(String pubDate) {
		this.pubDateString = pubDate;
	}	
	
	public void appendToTitle(String title) {
		if(getTitle()==null) setTitle(title);
		else setTitle(getTitle() + title);
	}

	public void appendToLink(String link) {
		if(getLink()==null) setLink(link);
		else setLink(getLink() + link);
	}

	public void appendToGuid(String guid) {
		if(getGuid()==null) setGuid(guid);
		else setGuid(getGuid() + guid);
	}

	public void appendToDescription(String description) {
		if(getDescription()==null) setDescription(description);
		else setDescription(getDescription() + description);
	}

	public void appendToPubDate(String pubDate) {
		if(this.pubDateString==null) setPubDate(pubDate);
		else setPubDate(this.pubDateString + pubDate);
	}	
	
	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}
	
	public void setPubDate() {
		try {
			setPubDate(getPubDateFormat().parse(this.pubDateString));
		} catch(ParseException e) {
			logger.warning("Failed to parse "+this.pubDateString+": "+e.toString());
		}
	}
	
	public String getMailSubject() {
		return getTitle();
	}
	
	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("guid", getGuid());
		jsonObject.addProperty("title", getTitle());
		jsonObject.addProperty("link", getLink());
		jsonObject.addProperty("pubDate", pubDateString);
		jsonObject.addProperty("description", getDescription());
		return jsonObject;
	}
}