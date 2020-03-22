package rss2mail.ibmcloud.pup.cloudant;

import java.util.Iterator;
import java.util.Vector;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import rss2mail.cloudant.BaseRssStatus;

public class PupRssStatus extends BaseRssStatus {
	
	private Vector<IncidentStatus> incidents = null;
	private Vector<MaintenanceStatus> maintenances = null;
	//private Vector<String> ended = null;
	//private Vector<String> investigating = null;

	public PupRssStatus(String _id) {
		super(_id);
	}
	
	public Vector<IncidentStatus> getIncidents() {
		return incidents;
	}
	
	public Vector<MaintenanceStatus> getMaintenances() {
		return maintenances;
	}
	
	public void setIncidents(Vector<IncidentStatus> incidents) {
		this.incidents = incidents;
	}
	
	public void setMaintenances(Vector<MaintenanceStatus> maintenances) {
		this.maintenances = maintenances;
	}

	public JsonObject toJson() {
		JsonObject jsonRssStatus = super.toJson();

		if(incidents!=null) {
			JsonArray jsonIncidents = new JsonArray();		
			Iterator<IncidentStatus> iterator = incidents.iterator();
			while(iterator.hasNext()) {
				jsonIncidents.add(iterator.next().toJsonObject());
			}
			jsonRssStatus.add("incidents", jsonIncidents);
		}
		
		if(maintenances!=null) {
			JsonArray jsonMaintenances = new JsonArray();	
			Iterator<MaintenanceStatus> iterator = maintenances.iterator();
			while(iterator.hasNext()) {
				jsonMaintenances.add(iterator.next().toJsonObject());
			}
			jsonRssStatus.add("maintenances", jsonMaintenances);
		}
		
		return jsonRssStatus;
	}
}
