package cloudant.utilities;

import java.util.logging.Logger;

//
import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.gson.JsonObject;

import cloudant.utilities.exceptions.CloudantException;
import system.utilities.PropertyManager;

//import cloudant.utilities.exceptions.CloudantException;

public class CloudantDatabase {

	public static Logger logger = 
			java.util.logging.Logger.getLogger(CloudantDatabase.class.getCanonicalName());

	public static String CLOUDANT_PROPERTIES="cloudant.properties";
	public static String CLOUDANT_ACCOUNT="cloudant.account";
	public static String CLOUDANT_USERNAME="cloudant.username";
	public static String CLOUDANT_PASSWORD="cloudant.password";
	public static String CLOUDANT_DATABASE="cloudant.database";
	
	//private String properties=System.getProperty(CLOUDANT_PROPERTIES, CLOUDANT_PROPERTIES);
	private String account;
	private String username;
	private String password;
	private String name;
	
	private Database database = null;
	
	public CloudantDatabase() throws CloudantException  {
		init();
		initDatabase();
	}
	
	public CloudantDatabase(String name) throws CloudantException  {
		init();
		setName(name);
		initDatabase();
	}
	
	public CloudantDatabase(String account, String username, String password, String name) throws CloudantException  {
		init();
		setAccount(account);
		setUsername(username);
		setPassword(password);
		setName(name);
		initDatabase();
	}
	
	protected void init() {
		
		setAccount(PropertyManager.getProperty(CLOUDANT_PROPERTIES, CLOUDANT_ACCOUNT));
		setUsername(PropertyManager.getProperty(CLOUDANT_PROPERTIES, CLOUDANT_USERNAME));
		setPassword(PropertyManager.getProperty(CLOUDANT_PROPERTIES, CLOUDANT_PASSWORD));
		setName(PropertyManager.getProperty(CLOUDANT_PROPERTIES, CLOUDANT_DATABASE));
		logger.info(toString());
		getDatabase();
	}
		
	protected void initDatabase() throws CloudantException {
		try {
			CloudantClient cloudClient = ClientBuilder.account(account)
                    .username(username)
                    .password(password)
                    .build();
			setDatabase(cloudClient.database(getName(), false));
			logger.info(getDatabase().info().toString());
		} catch(Exception e) {
			throw new CloudantException("Failed to get database", e);
		}
	}
	
	public Object find(String classname) 
	throws Exception
	{
		return find(Class.forName(classname));
	}

	public Object find(Class<?> object) 
	throws Exception
	{
		return find(object,object.getSimpleName());
	}

	public Object find(String classname, String _id) 
	throws Exception
	{
		return find(Class.forName(classname), _id);
	}
	
	public Object find(Class<?> object, String _id) 
	throws Exception
	{
		Object result = null;
		try {
			result = getDatabase().find(object,_id);
			logger.info("Found "+object.getCanonicalName());
		} catch(Exception e) {
			logger.warning(e.toString());
			throw e;
		}
		return result;
	}

	public void save(Object object) throws Exception {
		try {
			getDatabase().save(object);
		} catch(Exception e) {
			throw new CloudantException("Failed to save "+object.getClass().getName(), e);
		}
	}
	
	public void update(Object object) throws Exception {
		try {
			getDatabase().update(object);
		} catch(Exception e) {
			throw new CloudantException("Failed to update "+object.getClass().getName(), e);
		}
	}
	
	public void remove(Object object) throws Exception {
		try {
			getDatabase().remove(object);
		} catch(Exception e) {
			throw new CloudantException("Failed to save "+object.getClass().getName(), e);
		}
	}
	
	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("ACCOUNT",account);
		jsonObject.addProperty("USERNAME",username);
		if(password==null) jsonObject.addProperty("PASSWORD","UNSET");
		else jsonObject.addProperty("PASSWORD","SET");
		jsonObject.addProperty("DATABASE",name);
		return jsonObject;
	}
	
	public String toString() {
		return toJson().toString();
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

}
