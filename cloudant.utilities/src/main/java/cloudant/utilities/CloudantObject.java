package cloudant.utilities;

import com.google.gson.JsonObject;

public class CloudantObject {
	
	public CloudantObject(String _id) {
		set_id(_id);
	}

	private String _id;
	private String _rev;
	
	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String get_rev() {
		return _rev;
	}

	public void set_rev(String _rev) {
		this._rev = _rev;
	}
	
	public static CloudantObject find(CloudantDatabase database) throws Exception {
		return (CloudantObject)database.find(CloudantObject.class);
	}
	
	public static CloudantObject find(CloudantDatabase database, String _id) throws Exception {
		return (CloudantObject)database.find(CloudantObject.class,_id);
	}
	
	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("_id", get_id());
		if(get_rev()!=null) jsonObject.addProperty("_rev", get_rev());
		return jsonObject;
	}
	
	public String toString() {
		return toJson().toString();
	}

	/*
	public void save() throws CloudantException {
		database.save(this);
		database = db;
	}
	
	public void update() throws CloudantException {
		database.update(this);
	}
	
	public void remove() throws CloudantException {
		database.remove(this);
	}
	*/
}
