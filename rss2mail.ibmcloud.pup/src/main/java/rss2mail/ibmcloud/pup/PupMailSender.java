package rss2mail.ibmcloud.pup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import rss2mail.BaseMailSender;
import rss2mail.exceptions.RssException;
import rss2mail.ibmcloud.pup.cloudant.PupUserGroup;
import rss2mail.interfaces.RssItem;
import rss2mail.interfaces.UserGroup;

public class PupMailSender extends BaseMailSender {
			
	public PupMailSender() throws Exception 
	{	
		super();
	}

	public boolean filter(UserGroup userGroup, RssItem item) throws RssException {
		PupRssItem pupItem = (PupRssItem)item;
		PupUserGroup pupUserGroup = (PupUserGroup)userGroup;
		
		boolean bTypes = false;
		if(pupUserGroup.getTypes()!=null && pupItem.getType()!=null) {
			bTypes = true;
			String pupItemType = pupItem.getType().toLowerCase();
			Iterator<String> userGroupeTypes = pupUserGroup.getTypes().iterator();
			while(userGroupeTypes.hasNext()) {
				String userGroupType = userGroupeTypes.next();
				if(pupItemType.contains(userGroupType)) {
					bTypes=false;
					break;
				}
			}
		}
		if(bTypes) return true;
		
		boolean bRegions = false;
		if(pupUserGroup.getRegions()!=null && pupItem.getRegions()!=null) {
			bRegions = true;
			String pupItemRegions = pupItem.getRegions().toLowerCase();
			Iterator<String> userGroupRegions = pupUserGroup.getRegions().iterator();
			while(userGroupRegions.hasNext()) {
				String userGroupRregion = userGroupRegions.next().toLowerCase();
				if(pupItemRegions.contains(userGroupRregion)) {
					bRegions = false;
					break;
				}
			}
		}
		if(bRegions) return true;
		
		StringBuffer buffer=new StringBuffer();
		if(pupItem.getResources()!=null) buffer.append(pupItem.getResources());
		if(pupItem.getAffected()!=null) buffer.append(pupItem.getAffected());
		
		boolean bComponents = false;
		if(pupUserGroup.getComponents()!=null && buffer.length()>0) { 
			bComponents = true;
			String pupItemResources = buffer.toString().toLowerCase();
			Iterator<String> userGroupComponents = pupUserGroup.getComponents().iterator();
			while(userGroupComponents.hasNext()) {
				String userGroupComponent = userGroupComponents.next().toLowerCase();
				if(pupItemResources.contains(userGroupComponent)) {
					bComponents = false;
					break;
				} 
			}
		}
		return bComponents;
	}

	public String getContent(UserGroup userGroup, RssItem item) {
		PupRssItem pupItem = (PupRssItem)item;
		PupUserGroup pupUserGroup = (PupUserGroup)userGroup;
		
		StringBuffer buffer = new StringBuffer();
		
		SimpleDateFormat dateFormat=new SimpleDateFormat(userGroup.getDateformat());
		dateFormat.setTimeZone(TimeZone.getTimeZone(userGroup.getTimezone()));
		
		buffer.append(htmlString(null, "Type", pupItem.getType()));
		buffer.append(htmlString(null, "Regions", pupItem.getRegions()));
		buffer.append(htmlString(null, "Resources", pupItem.getResources()));
	
		if(pupItem.isTypeIncident()) {
			buffer.append(htmlString(null, "Services/Components", pupItem.getAffected()));
		}
		
		buffer.append(htmlDate(null, "Even start time", dateFormat, pupItem,PupRssItem.LABEL_EVENTSTARTTIME));
		buffer.append(htmlDate(null, "Event end time", dateFormat, pupItem,PupRssItem.LABEL_EVENTENDTIME));
		buffer.append(htmlDate(null, "Last update time", dateFormat, pupItem,PupRssItem.LABEL_LASTUPDATETIME));
		
		if(pupItem.isTypeIncident() || pupItem.isTypeMaintenance())
			buffer.append(htmlString(null,pupItem.getType().replaceAll("<br />", "")+"ID", pupItem,PupRssItem.LABEL_ID));
		
		if(pupItem.isTypeIncident()) {
			buffer.append(htmlString(null, "Impact", pupItem,PupRssItem.LABEL_IMPACT));

			String status = pupItem.getSringLabel(PupRssItem.LABEL_STATUS);
			for(int i=0; i<pupItem.sizeStatusDates(); i++) {
				status = pupItem.getStatusDate(i).changeDateFormat(status, dateFormat);
			}
			buffer.append(htmlString(null, "Status: ", status));
		} else if(pupItem.isTypeMaintenance()) {
			buffer.append(htmlString(null, "Update Description", pupItem.getUpdateDesc()));
			buffer.append(htmlString(null, "Maintenance Duration", pupItem.getDuration()));
			buffer.append(htmlString(null, "Disruption Type", pupItem.getDisrupType()));
			buffer.append(htmlString(null, "Disruption Description", pupItem.getDisruptDesc()));
			buffer.append(htmlString(null, "Disruption Duration", pupItem.getDisruptDuration()));
		} else {
			buffer.append(htmlString(null, null, pupItem.getProlog()));
		}

		buffer.append("<p><strong>categories: </strong> ");
		for(int i=0;i<pupItem.sizeCategories();i++) {
			buffer.append(pupItem.getCategory(i)).append(", ");
		}
		buffer.append("</p>");
		
		buffer.append("<p>Filters:");
		if(pupUserGroup.getTypes()!=null) {
			buffer.append("<br>- Types: ");
			Iterator<String> types = pupUserGroup.getTypes().iterator();
			while(types.hasNext()) {
				String type = types.next();
				buffer.append(type+",");
			}
		}
		if(pupUserGroup.getRegions()!=null) {
			buffer.append("<br>- Regions: ");
			Iterator<String> regions = pupUserGroup.getRegions().iterator();
			while(regions.hasNext()) {
				String region = regions.next();
				buffer.append(region+",");
			}
		}
		if(pupUserGroup.getComponents()!=null) {
			buffer.append("<br>- Components: ");
			Iterator<String> components = pupUserGroup.getComponents().iterator();
			while(components.hasNext()) {
				String component = components.next();
				buffer.append(component+",");
			}
		}
		buffer.append("</p>");
		
		return buffer.toString();
	}
	
	public String htmlDate(String id, String name, SimpleDateFormat dateFormat, PupRssItem pupRssItem, String label) {
		if(pupRssItem.containsDateLabel(label)){
			Date date = null;
			ContainedDate containedDate = pupRssItem.getLabeledDate(label);
			if(containedDate.getDate()==null) {
				return super.htmlString(id, name, containedDate.getSource());
			}
			date = containedDate.getDate();
			return super.htmlDate(id, name, dateFormat, date);
		}
		return super.htmlString(id, name, "");
	}

	public String htmlString(String id, String name, PupRssItem pupRssItem, String label) {
		String value = null;
		if(pupRssItem.containsStringLabel(label)) {
			value = pupRssItem.getSringLabel(label);
		}
		return super.htmlString(id, name, value);
	}
}
