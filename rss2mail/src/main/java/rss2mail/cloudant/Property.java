package rss2mail.cloudant;

import com.google.gson.JsonObject;

public class Property {
	private String name;
	private String value;
	
	public Property() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", getName());
		jsonObject.addProperty("value", getValue());
		return jsonObject;
	}
	
	public String toString() {
		return toJson().toString();
	}
}
