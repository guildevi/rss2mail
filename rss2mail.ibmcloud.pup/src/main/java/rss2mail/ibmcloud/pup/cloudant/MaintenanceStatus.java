package rss2mail.ibmcloud.pup.cloudant;

import java.util.Date;

public class MaintenanceStatus extends PupRssItemStatus {
	
	public static String PLANNED="PLANNED";
	public static String CANCELLED="CANCELLED";
	public static String COMPLETED="COMPLETED";
	
	public MaintenanceStatus() {
		super();
	}

	public MaintenanceStatus(String guid, String status) {
		super(guid,status);
	}

	public MaintenanceStatus(String guid, String status, Date date) {
		super(guid,status,date);
	}
	
	public boolean isPlanned() {
		return getStatus().equals(PLANNED);
	}

	public boolean isCancelled() {
		return getStatus().equals(CANCELLED);
	}

	public boolean isCompleted() {
		return getStatus().equals(COMPLETED);
	}

	public void setPlanned() {
		setStatus(PLANNED);
	}
	
	public void setCancelled() {
		setStatus(CANCELLED);
	}
	
	public void setCompleted() {
		setStatus(COMPLETED);
	}

	public void setPlanned(Date date) {
		setStatus(PLANNED,date);
	}
	
	public void setCancelled(Date date) {
		setStatus(CANCELLED,date);
	}
	
	public void setCompleted(Date date) {
		setStatus(COMPLETED,date);
	}}
