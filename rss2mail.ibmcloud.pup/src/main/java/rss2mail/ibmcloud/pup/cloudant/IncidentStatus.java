package rss2mail.ibmcloud.pup.cloudant;

import java.util.Date;

public class IncidentStatus extends PupRssItemStatus {
	
	public static String INVESTIGATING="INVESTIGATING";
	public static String ENDED="ENDED";
	public static String RESOLVED="RESOLVED";

	public IncidentStatus() {
		super();
	}
	
	public IncidentStatus(String guid) {
		super(guid,INVESTIGATING);
	}
	
	public IncidentStatus(String guid,String status) {
		super(guid,status);
	}

	public IncidentStatus(String guid,String status,Date date) {
		super(guid,status,date);
	}

	public boolean isInvestigating() {
		return getStatus().equals(INVESTIGATING);
	}
	
	public boolean isEnded() {
		return getStatus().equals(ENDED);
	}

	public boolean isResolved() {
		return getStatus().equals(RESOLVED);
	}
	
	public void setInvestigating() {
		setStatus(INVESTIGATING);
	}

	public void setEnded() {
		setStatus(ENDED);
	}

	public void setResolved() {
		setStatus(RESOLVED);
	}

	public void setInvestigating(Date date) {
		setStatus(INVESTIGATING, date);
	}

	public void setEnded(Date date) {
		setStatus(ENDED, date);
	}

	public void setResolved(Date date) {
		setStatus(RESOLVED, date);
	}


}
