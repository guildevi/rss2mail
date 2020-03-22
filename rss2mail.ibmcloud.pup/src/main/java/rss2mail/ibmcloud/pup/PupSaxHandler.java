package rss2mail.ibmcloud.pup;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import org.xml.sax.SAXException;

import rss2mail.BaseSaxHandler;
import rss2mail.ibmcloud.pup.cloudant.IncidentStatus;
import rss2mail.ibmcloud.pup.cloudant.MaintenanceStatus;
import rss2mail.ibmcloud.pup.cloudant.PupRssStatus;
import rss2mail.interfaces.RssItem;

public class PupSaxHandler extends BaseSaxHandler {

	public static Logger logger = 
			Logger.getLogger(BaseSaxHandler.class.getCanonicalName());

	private final static int INAME_CATEGORY=110;
	
	private Date oldest=new Date();
	private Hashtable<String, IncidentStatus> incidentStatus = new Hashtable<String, IncidentStatus>();
	private Hashtable<String, MaintenanceStatus> maintenanceStatus = new Hashtable<String, MaintenanceStatus>();
		 
	public PupSaxHandler() throws Exception {
		super();
		
		if(getPupRssStatus().getIncidents()!=null) {
			Iterator<IncidentStatus> cloudantIncidents = getPupRssStatus().getIncidents().iterator();
			while(cloudantIncidents.hasNext()) {
				IncidentStatus cloudantIncident = cloudantIncidents.next();
				incidentStatus.put(cloudantIncident.getGuid(), cloudantIncident);
			}
		}

		if(getPupRssStatus().getMaintenances()!=null) {
			Iterator<MaintenanceStatus> cloudantMaintenances = getPupRssStatus().getMaintenances().iterator();
			while(cloudantMaintenances.hasNext()) {
				MaintenanceStatus cloudantMaintenance = cloudantMaintenances.next();
				maintenanceStatus.put(cloudantMaintenance.getGuid(), cloudantMaintenance);
			}
		}
	}
	
    public int parseQName(String qName) {
    		int iName=INAME_UNKNOWN;
    		if(qName.equalsIgnoreCase("category")) {
        		iName=INAME_CATEGORY;
        } else {
        		iName=super.parseQName(qName);
        }
		return iName;
    }
        
    public void endElement(String uri, String localName, String qName) throws SAXException {
    		if(iNamesTree[treeLevel]==INAME_ITEM) {
    			endElementItem();
    			treeLevel--;
		} else if(iNamesTree[treeLevel]==INAME_CHANNEL) {
			
			Vector<IncidentStatus> vectorIncidents = new Vector<IncidentStatus>();
			Enumeration<IncidentStatus> cloudantIncidents = incidentStatus.elements();
			while(cloudantIncidents.hasMoreElements()) {
				IncidentStatus cloudantIncident = cloudantIncidents.nextElement();
				if(cloudantIncident.getDate().before(getOldest())) {
					vectorIncidents.add(cloudantIncident);
				}
			}
			getPupRssStatus().setIncidents(vectorIncidents);
			
			Vector<MaintenanceStatus> vectorMaintenances = new Vector<MaintenanceStatus>();
			Enumeration<MaintenanceStatus> cloudantMaintenances = maintenanceStatus.elements();
			while(cloudantMaintenances.hasMoreElements()) {
				MaintenanceStatus cloudantMaintenance = cloudantMaintenances.nextElement();
				if(cloudantMaintenance.getDate().before(getOldest())) {
					vectorMaintenances.add(cloudantMaintenance);
				}
			}
			getPupRssStatus().setMaintenances(vectorMaintenances);
		
			treeLevel--;
		} else super.endElement(uri, localName, qName);
    }
    
    public void endElementItem() {
		PupRssItem pupRssItem = getPupRssItem();
		pupRssItem.setDescription(pupRssItem.getDescription().replaceAll("&nbsp;"," "));
		
		boolean latest = false;
		Date lastDate=pupRssItem.getLastDate();
		if(lastDate.after(getLatest())) {
			setLatest(lastDate);
			latest=true;
			logger.info(lastDate+" IS LATEST: "+pupRssItem.getGuid()+" - "+getTitle());
		}
		if(getOldest().before(lastDate)) {
			logger.info(lastDate+" IS OLDEST: "+pupRssItem.getGuid()+" - "+getTitle());
			setOldest(lastDate);
		}
				
		String guid = pupRssItem.getGuid();
		String cloudantStatus = pupRssItem.getCloudantStatus();
		if(pupRssItem.isTypeIncident()) {
			if(incidentStatus.containsKey(guid)) {
				IncidentStatus cloudantIncident = incidentStatus.get(guid);
				if(!cloudantStatus.equals(cloudantIncident.getStatus())) {
					cloudantIncident.setStatus(cloudantStatus,pupRssItem.getLastDate());
					addToRssItems(pupRssItem,latest);
					logger.info(pupRssItem.getGuid()+" MODIFIED ("+getTitle()+")");
				} else {
					logger.info(pupRssItem.getGuid()+" UNMODIFIED ("+getTitle()+")");
				}
			} else {
				IncidentStatus cloudantIncident = new IncidentStatus(guid,cloudantStatus,pupRssItem.getLastDate());
				incidentStatus.put(guid, cloudantIncident);
				addToRssItems(pupRssItem,latest);
				logger.info(pupRssItem.getGuid()+" NEW ("+getTitle()+")");
			}
		} else if(pupRssItem.isTypeMaintenance())   {
			if(maintenanceStatus.containsKey(guid)) {
				MaintenanceStatus cloudantMaintenance = maintenanceStatus.get(guid);
				if(!cloudantStatus.equals(cloudantMaintenance.getStatus())) {
					cloudantMaintenance.setStatus(cloudantStatus,pupRssItem.getLastDate());
					addToRssItems(pupRssItem,latest);
					logger.info(pupRssItem.getGuid()+" MODIFIED ("+getTitle()+")");
				} else {
					logger.info(pupRssItem.getGuid()+" UNMODIFIED ("+getTitle()+")");
				}
			} else {
				MaintenanceStatus cloudantMaintenance = new MaintenanceStatus(guid,cloudantStatus,pupRssItem.getLastDate());
				maintenanceStatus.put(guid, cloudantMaintenance);
				addToRssItems(pupRssItem,latest);
				logger.info(pupRssItem.getGuid()+" NEW ("+getTitle()+")");

			}
		} else if(keepLatest() || lastDate.after(getPupRssStatus().getTimestamp())) {
			addToRssItems(pupRssItem,latest);
			//Logger.info(pupRssItem.getGuid()+" IS "+getTitle());
		}
    }
    
    public void setItemAttribute(char ch[], int start, int length) {
    		switch (iNamesTree[treeLevel]) {
			case INAME_CATEGORY:
				getPupRssItem().addCategory(getData(ch,start,length));
				break;
			case INAME_DESCRIPTION:
				getPupRssItem().appendToDescription(getData(ch,start,length));
				break;
			default:
				super.setItemAttribute(ch, start, length);
    		}
    }
    
    public PupRssItem getPupRssItem() {
		return (PupRssItem)getRssItem();
    }

    public PupRssStatus getPupRssStatus() {
		return (PupRssStatus)getRssStatus();
    }

    private String getTitle() {
    		StringBuffer buffer = new StringBuffer();
    		PupRssItem pupRssItem = getPupRssItem();
    		if(pupRssItem.isTypeIncident()&&pupRssItem.isTypeMaintenance()) {
    			buffer.append("(").append(pupRssItem.getGuid()).append(") - ");
    		}
    		buffer.append(getPupRssItem().getTitle());
    		return buffer.toString();
    }

	public Hashtable<String, IncidentStatus> getIncidentStatus() {
		return incidentStatus;
	}

	public void setIncidentStatus(Hashtable<String, IncidentStatus> incidentStatus) {
		this.incidentStatus = incidentStatus;
	}

	public Hashtable<String, MaintenanceStatus> getMaintenanceStatus() {
		return maintenanceStatus;
	}

	public void setMaintenanceStatus(Hashtable<String, MaintenanceStatus> maintenanceStatus) {
		this.maintenanceStatus = maintenanceStatus;
	}
	
	protected void addToRssItems(PupRssItem pupRssItem, boolean isLatest) {
		if(keepLatest()) {
			if(isLatest) {
				if(getRssItems().size()==0) getRssItems().add(pupRssItem);
				else getRssItems().set(0, pupRssItem);
			}
		} else addToRssItems((RssItem)pupRssItem);
	}

	public Date getOldest() {
		return oldest;
	}

	public void setOldest(Date oldest) {
		this.oldest = oldest;
	}
}