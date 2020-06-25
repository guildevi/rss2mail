package cloudant.utilities;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cloudant.utilities.exceptions.CloudantException;

public class CloudantProperties extends CloudantObject {

	private Set<Property> properties ;
	
	public static CloudantProperties find(CloudantDatabase database, String _id) throws Exception {
		return (CloudantProperties)database.find(CloudantProperties.class, _id);
	}
	
	public Properties toProperties() 
	throws CloudantException {
		Properties result = new Properties();
		for(Property property : getProperties()) {
			result.put(property.getName(), property.getValue());
		}
		return result;
	}
		
	public CloudantProperties(String _id) {
		super(_id);
	}

	public Set<Property> getProperties() {
		return properties;
	}

	public void setProperties(Set<Property> properties) {
		this.properties = properties;
	}

	public void setProperties(Properties _properties) throws CloudantException {
		Set<Property> set = new HashSet<Property>();
		Enumeration<Object> keys = _properties.keys();
		while(keys.hasMoreElements()) {
			Property property = new Property();
			String key = (String)keys.nextElement();
			property.setName(key);
			property.setValue(_properties.getProperty(key));
			set.add(property);
		}
		setProperties(set);
	}
	
	public JsonObject toJson( ) {
		JsonObject jsonObject = super.toJson();
		if(getProperties()!=null) {
			JsonArray jsonProperties = new JsonArray();
			for(Property property : getProperties()) {
				JsonObject jsonProperty = new JsonObject();
				jsonProperty.addProperty("name",property.getName());
				jsonProperty.addProperty("value",property.getName());
				jsonProperties.add(jsonProperty);
			}
			jsonObject.add("properties", jsonProperties);
		}
		return jsonObject;
	}	
	
	public String getValue(String name) {
		String value = null;
		for(Property property : getProperties()) {
			if(property.getName().equals(name)) {
				value=property.getValue();
			}
		}
		return value;
	}

}
