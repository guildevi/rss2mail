package rss2mail.cloudant;

import com.google.gson.JsonObject;

import cloudant.utilities.CloudantElement;

public class BaseUser implements CloudantElement{
	
	private String address;
	private String personal;
	
	public BaseUser() {
		super();
	}
	public BaseUser(String address, String personal) {
		setAddress(address);
		setPersonal(personal);
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPersonal() {
		return personal;
	}
	public void setPersonal(String personal) {
		this.personal = personal;
	}
	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("personnal", getPersonal());
		jsonObject.addProperty("address", getAddress());
		return jsonObject;
	}

}
