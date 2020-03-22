package rss2mail.ibmcloud.pup;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import java.util.TreeMap;
import java.util.Vector;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import rss2mail.BaseRssItem;
import rss2mail.exceptions.InvalidSourceDateFormatException;
import rss2mail.exceptions.PrefixNotFoundException;
import rss2mail.exceptions.RssException;
import rss2mail.exceptions.SuffixNotFoundException;
import rss2mail.ibmcloud.pup.cloudant.IncidentStatus;
import rss2mail.ibmcloud.pup.cloudant.MaintenanceStatus;

/*
 * Stores an RSS feed
 */
public class PupRssItem extends BaseRssItem
{	
	public static int ALL=-1;
	public static int NONE=0;
	
	public static String LABEL_BEGIN = "PROLOG";
	public static String LABEL_AFFECTED = "SERVICES/COMPONENTS AFFECTED";
	public static String LABEL_IMPACT = "IMPACT";
	public static String LABEL_STATUS = "STATUS";
	public static String LABEL_TYPE = "Type";
	public static String LABEL_ID = "ID";
	public static String LABEL_REGIONS = "Regions";
	public static String LABEL_RESOURCES = "Resources";
	public static String LABEL_EVENTSTARTTIME = "Outage Start";
	public static String LABEL_EVENTENDTIME = "Outage End";
	public static String LABEL_LASTUPDATETIME = "Update Time";
	
		
	public static String LABEL_UPDATEDESC = "Update Description";
	public static String LABEL_DURATION = "Maintenance Duration";
	public static String LABEL_TYPEDISRUPT = "Type of Disruption";
	public static String LABEL_DISRUPTDESC = "Disruption Description";
	public static String LABEL_DISRUPTDURATION = "Disruption Duration";
	
	public static String LABEL_COMPONENT = "Component";
	
	public static String[] LABEL_PREFIXES  = {LABEL_AFFECTED,LABEL_IMPACT,LABEL_STATUS,
			LABEL_TYPE,LABEL_ID,LABEL_REGIONS,LABEL_RESOURCES,
			LABEL_LASTUPDATETIME,LABEL_EVENTSTARTTIME,LABEL_EVENTENDTIME, LABEL_COMPONENT,
			LABEL_UPDATEDESC, LABEL_DURATION, LABEL_TYPEDISRUPT, LABEL_DISRUPTDESC, LABEL_DISRUPTDURATION};

	public static String[] LABEL_LABELEDDATES  = {LABEL_EVENTSTARTTIME,LABEL_EVENTENDTIME};
	
	public static String TYPE_INCIDENT="incident";
	public static String TYPE_MAINTENANCE="maintenance";
	public static String TYPE_ANNOUNCEMENT="announcement";
	public static String TYPE_SECURITY="security";
	
	private Vector<String> components = null;
    private Hashtable<String, String> stringLabels = null;
    private Hashtable<String, LabeledDate> dateLabels = null;
    private Vector<StatusDate> statusDates = null;

    public PupRssItem() {
    		super();
    }
    
    @Override
    public JsonObject toJson() {
    	JsonObject jsonOject = super.toJson();
    	
    	JsonArray jsonArray = new JsonArray();
    	Iterator<String> iterator = components.iterator();
    	while(iterator.hasNext()) {
    		jsonArray.add(iterator.next());
    	}
    	jsonOject.add("components", jsonArray);
    	
    	return jsonOject;
    }

    public String getGuid() {
    		if(isTypeAnnouncement()) return "Announcement";
    		else if (isTypeSecurityBulletin()) return "Security Bulletin";
    		else return super.getGuid();
    }

	public void initStringLabels() {
		if(stringLabels == null) stringLabels = new Hashtable<String, String>();
	}
	
	
	public String getSringLabel(String label) {
		if(stringLabels==null) return null;
		else return stringLabels.get(label);
	}
	
	public void addStringLabel(String label, String data) {
		initStringLabels();
		stringLabels.put(label, data);
	}
	
	public int sizeStringLabels() {
		if(stringLabels==null) return 0;
		return stringLabels.size();
	}
	
	public boolean containsStringLabel(String label ) {
		if(stringLabels==null) return false;
		return stringLabels.containsKey(label);
	}
	
	public Enumeration<String> enumStringLabelsElenents() {
		return stringLabels.elements();
	}
	
	public Enumeration<String> enumStringLabelsLabels() {
		return stringLabels.keys();
	}
	
	public LabeledDate getLabeledDate(String label) {
		return dateLabels.get(label);
	}
	
	public void initDateLabel() {
		if(dateLabels==null) dateLabels = new Hashtable<String,LabeledDate>();
	}
	
	public void addDateLabel(String label, LabeledDate date) {
		initDateLabel();
		dateLabels.put(label, date);
	}
	
	public int sizeLabeledDate() {
		if(dateLabels==null) return 0;
		return dateLabels.size();
	}
	
	public boolean containsDateLabel(String label ) {
		if(dateLabels==null) return false;
		return dateLabels.containsKey(label);
	}
	
	public Enumeration<LabeledDate> enumDateLabelsElenents() {
		return dateLabels.elements();
	}
	
	public Enumeration<String> enumDateLabelsLabels() {
		return dateLabels.keys();
	}
	
	public void initLastUpdateTime() {
		try {
			addDateLabel(LABEL_LASTUPDATETIME,new LabeledDate(getDescription(),LABEL_LASTUPDATETIME));
		} catch(RssException e) {
			logger.warning(e.toString());
		}
	}
	
	public void initEventEndTime() {
		try {
			addDateLabel(LABEL_EVENTENDTIME,new LabeledDate(getDescription(),LABEL_EVENTENDTIME));
		} catch(RssException e) {
			logger.finest(e.toString());
		}
	}
	
	public void initLabels() {
		
		TreeMap<Integer, String> labels = new TreeMap<Integer,String>();
		String description = getDescription().replaceAll("<b>","").replaceAll("</b>","")
				.replaceAll("<strong>","").replaceAll("</strong>","")
				.replaceAll("&nbsp;","");
		
		// look for each label position 
		int typeIndex=-1;
		for(int i=0; i<LABEL_PREFIXES.length; i++) {
			String label = LABEL_PREFIXES[i];
			int index = description.indexOf(label+":");
			if(index>=0) {
				labels.put(new Integer(index), label);
				if(label.equals(LABEL_TYPE)) typeIndex=index;
			}
		}
		
		if(typeIndex!=-1) {
			int index = description.indexOf(LABEL_TYPE, typeIndex+LABEL_TYPE.length());
			if(index>typeIndex) labels.put(new Integer(index), LABEL_TYPE);
		}
		
		java.util.Iterator<Entry<Integer, String>> iterator = labels.entrySet().iterator();
		Entry<Integer, String> entry = iterator.next();
		String label = entry.getValue();
		int offset = entry.getKey().intValue();
		int endIndex = offset;
		if(offset>0) {
			String data = description.substring(0, offset);
			addStringLabel(LABEL_BEGIN, data);
		}
		do {
			try {
				entry = iterator.next();
				endIndex=entry.getKey().intValue();
				int semiColon = description.indexOf(":", offset);
				if(semiColon==-1) {
					logger.warning(label+" not followed by semicolon");
				} if(semiColon>endIndex){
					logger.warning(label+"'s semicolon after "+entry.getValue());
				} else {
					String data = description.substring(semiColon+1, endIndex);
					addStringLabel(label, data);
				}
			} catch(IndexOutOfBoundsException e) {
				logger.severe(e.toString());
			} finally {
				label = entry.getValue();
				offset = endIndex;
			}
		} while(iterator.hasNext());
		
		for(int i=0; i<LABEL_LABELEDDATES.length;i++) {
			try {
				label = LABEL_LABELEDDATES[i];
				LabeledDate dateValue = new LabeledDate(getDescription(),label);
				addDateLabel(label,dateValue);
			} catch(InvalidSourceDateFormatException e) {
				logger.warning(e.toString());
			} catch(PrefixNotFoundException e) {
				logger.warning(e.toString());
			} catch(SuffixNotFoundException e) {
				logger.warning(e.toString());
			} catch(RssException e) {
				logger.warning(e.toString());
			}
		}
		
		Enumeration<LabeledDate> enumDateLabels = dateLabels.elements();
		while(enumDateLabels.hasMoreElements()) {
			LabeledDate labeledDate = enumDateLabels.nextElement();
			logger.info(labeledDate.getLabel()+" "+labeledDate.getDate());
		}

	}
	
	public void addStatusDate(StatusDate statusDate) {
		if(statusDates==null) statusDates = new Vector<StatusDate>();
		statusDates.addElement(statusDate);
	}
	
	public StatusDate getStatusDate(int index) {
		if(statusDates==null) return null;
		return statusDates.elementAt(index);
	}
	
	public int sizeStatusDates() {
		if(statusDates==null) return 0;
		return statusDates.size();
	}
	
	public StatusDate getLatestStatusDates() {
		if(statusDates==null) return null;
		return statusDates.get(statusDates.size()-1);
	}
	
	public Date getLastDate() {
		/*if(isTypeIncident() && getEventEndTime()!=null) {
			return getEventEndTime();
		} else */if(getLastUpdateTime()!=null) {
			return getLastUpdateTime();
		} else return getPubDate();
	}
		
	
	public void init() {
		initLabels();
		initStatusDates();
	}
	
	public void initStatusDates() {
		String status = getStatus();
		if(status == null) return;
		int offset = 0;
		boolean cont = true;
		do { 
			try {
				StatusDate statusDate = new StatusDate(status,offset);
				addStatusDate(statusDate);
				offset=statusDate.getEnd()+StatusDate.SUFFIX.length();
			} catch(InvalidSourceDateFormatException e) {
				logger.warning(e.getMessage());
				offset=getDescription().indexOf(StatusDate.SUFFIX, offset);
				if(offset==-1) cont=false;
				else offset = offset + StatusDate.SUFFIX.length();
			} catch(RssException e) {
				logger.finest(e.toString());
				cont=false;
			} 
		} while(cont);
		
		if(statusDates!=null) {
			for(int i=0; i<statusDates.size(); i++) {
				StatusDate statusDate = statusDates.get(i);
				logger.info(LABEL_STATUS+" "+i+" - "+statusDate.getDate());
			}
		}

	}

	public static String getLabelPrefix(String label) {
		return "<strong>"+label+": </strong>";
	}
	
	public void addCategory(String category) {
		if(getCategories()==null) initCategories();
		getCategories().addElement(category);
	}

	public void initCategories() {
		setCategories(new Vector<String>());
	}

	public void setCategories(Vector<String> categories) {
		this.components=categories;
	}

	public Vector<String> getCategories() {
		return components;
	}

	public boolean containsCategory(String category) {
		return getCategories().contains(category);
	}

	public boolean containsCategoryIgnoreCase(String category) {
		if(getCategories()==null) return false;
		category=category.toLowerCase();
		for(int i=0; i<getCategories().size(); i++) {
			if(components.get(i).toLowerCase().contains(category)) {			
				return true;
			}
		}
		return false;
	}

	public String getCategory(int i) {
		if(i>=sizeCategories()) return null;
		else return getCategories().get(i);
	}

	
	public int sizeCategories() {
		if(getCategories()==null) return 0;
		else return getCategories().size();
	}
	
	public boolean containsCategories(Vector<String> testCategories) {
		for(int i=0; i<testCategories.size(); i++) {
			if(containsCategory(testCategories.elementAt(i))) return true;
		}
		return false;
	}
	
	public boolean containsCategoriesIgnoreCase(Vector<String> testCategories) {
		for(int i=0; i<testCategories.size(); i++) {
			if(containsCategoryIgnoreCase(testCategories.elementAt(i))) return true;
		}
		return false;
	}
	
	public String getType() {
		return getSringLabel(LABEL_TYPE);
	}

	public String getRegions() {
		return getSringLabel(LABEL_REGIONS);
	}

	public Date getEventStartTime() {
		Date date = null;
		if(containsDateLabel(LABEL_EVENTSTARTTIME)) {
			date = getLabeledDate(LABEL_EVENTSTARTTIME).getDate();
		}
		return date;
	}

	public Date getEventEndTime() {
		Date date = null;
		if(!containsDateLabel(LABEL_EVENTENDTIME)) initEventEndTime();
		if(containsDateLabel(LABEL_EVENTENDTIME)) {
			date = getLabeledDate(LABEL_EVENTENDTIME).getDate();
		}
		return date;
	}
	
	public boolean isEnded() {
		if(getEventEndTime()==null) return false;
		else return true;
	}
	
	public boolean isResolved() {
		if(getTitle().contains("RESOLVED:")) return true;
		else return false;
	}
	
	public boolean isCompleted() {
		if(getTitle().contains("COMPLETED:")) return true;
		else return false;
	}
	
	public boolean isCancelled() {
		if(getTitle().contains("CANCELLED:")) return true;
		else return false;
	}
	
	public boolean isInvestigating() {
		return !(isEnded()||isResolved());
	}
	
	public Date getLastUpdateTime() {
		Date date = null;
		if(!containsDateLabel(LABEL_LASTUPDATETIME)) initLastUpdateTime();
		if(containsDateLabel(LABEL_LASTUPDATETIME)) {
			date = getLabeledDate(LABEL_LASTUPDATETIME).getDate();
		} 
		return date;
	}

	public String getResources() {
		return getSringLabel(LABEL_RESOURCES);
	}

	public String getAffected() {
		return getSringLabel(LABEL_AFFECTED);
	}

	public String getID() {
		return getSringLabel(LABEL_ID);
	}
	
	public String getImpact() {
		return getSringLabel(LABEL_IMPACT);
	}

	public String getStatus() {
		return getSringLabel(LABEL_STATUS);
	}

	public String getUpdateDesc() {
		String data = getSringLabel(LABEL_UPDATEDESC);
		if(data == null) data = getSringLabel(LABEL_BEGIN);
		return data;
	}

	public String getDuration() {
		return getSringLabel(LABEL_DURATION);
	}

	public String getDisrupType() {
		return getSringLabel(LABEL_TYPEDISRUPT);
	}

	public String getDisruptDesc() {
		return getSringLabel(LABEL_DISRUPTDESC);
	}

	public String getDisruptDuration() {
		return getSringLabel(LABEL_DISRUPTDURATION);
	}

	public String getProlog() {
		return getSringLabel(LABEL_BEGIN);
	}


	public boolean isTypeIncident() {
		if(getCategory(0)!=null)	return getCategory(0).indexOf(TYPE_INCIDENT)>=0;
		else return false;
	}
	
	public boolean isTypeMaintenance() {
		if(getCategory(0)!=null)	return getCategory(0).indexOf(TYPE_MAINTENANCE)>=0;
		else return false;
	}	
	
	public boolean isTypeAnnouncement() {
		if(getCategory(0)!=null)	return getCategory(0).indexOf(TYPE_ANNOUNCEMENT)>=0;
		else return false;
	}
	
	public boolean isTypeSecurityBulletin() {
		if(getCategory(0)!=null)	return getCategory(0).indexOf(TYPE_SECURITY)>=0;
		else return false;
	}
	
	public String getCloudantStatus() {
		if(isTypeIncident()) {
			if(isResolved()) return IncidentStatus.RESOLVED;
			else if(isEnded()) return IncidentStatus.ENDED;
			else return IncidentStatus.INVESTIGATING;
		} else if(isTypeMaintenance()) {
			if(isCompleted()) return MaintenanceStatus.COMPLETED;
			else if(isCancelled()) return MaintenanceStatus.CANCELLED;
			else return MaintenanceStatus.PLANNED;
		} else return "UNKNOWN";
	}
	
	public String getMailSubject() {
		StringBuffer buffer = new StringBuffer();
		if(isTypeIncident()||isTypeMaintenance()) {
			buffer.append('[');
			buffer.append(getGuid());
			buffer.append("] ");
		}
		buffer.append(getTitle());
		return buffer.toString();
	}
}