package rss2mail.ibmcloud.pup.cloudant;

import java.util.Vector;

import com.google.gson.JsonObject;

import rss2mail.cloudant.BaseUserGroup;

public class PupUserGroup extends BaseUserGroup {

	private Vector<String> types;
	private Vector<String> regions;
	private Vector<String> components;
	
	public Vector<String> getTypes() {
		return types;
	}
	public void setTypes(Vector<String> types) {
		this.types = types;
	}
	public int sizeTypes( ) {
		if(types==null) return 0;
		return types.size();
	}
	
	public Vector<String> getRegions() {
		return regions;
	}
	public void setRegions(Vector<String> regions) {
		this.regions = regions;
	}	
	public int sizeRegions( ) {
		if(regions==null) return 0;
		return regions.size();
	}
			
	public Vector<String> getComponents() {
		return components;
	}
	public void setComponents(Vector<String> components) {
		this.components = components;
	}
		
	public JsonObject toJson() {
		JsonObject jsonObject = super.toJson();
		jsonObject.addProperty("language", getLanguage());
		jsonObject.addProperty("timezone", getTimezone());
		jsonObject.addProperty("dateformat", getDateformat());
						
		if(types!=null) jsonObject.add("types", BaseUserGroup.toJsonArray(types));
		if(regions!=null) jsonObject.add("regions", BaseUserGroup.toJsonArray(regions));
		if(components!=null) jsonObject.add("components", BaseUserGroup.toJsonArray(components));
				
		return jsonObject;
	}
}
