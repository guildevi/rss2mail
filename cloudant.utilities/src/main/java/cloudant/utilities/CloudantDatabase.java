package cloudant.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

//
import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.DbInfo;
import com.google.gson.JsonObject;

import cloudant.utilities.exceptions.CloudantException;

//import cloudant.utilities.exceptions.CloudantException;

public class CloudantDatabase {

	public static Logger logger = 
			java.util.logging.Logger.getLogger(CloudantDatabase.class.getCanonicalName());

	public static String PROP_CLOUDANT_PROPERTIES="cloudant.properties";
	public static String PROP_CLOUDANT_ACCOUNT="cloudant.account";
	public static String PROP_CLOUDANT_USERNAME="cloudant.username";
	public static String PROP_CLOUDANT_PASFSWORD="cloudant.password";
	public static String PROP_CLOUDANT_DATABASE="cloudant.database";
	
	private String properties=System.getProperty(PROP_CLOUDANT_PROPERTIES, PROP_CLOUDANT_PROPERTIES);
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
		initFromResource();
		initFromFile();
		setAccount(System.getProperty(PROP_CLOUDANT_ACCOUNT, getAccount()));
		setUsername(System.getProperty(PROP_CLOUDANT_USERNAME, getAccount()));
		setPassword(System.getProperty(PROP_CLOUDANT_ACCOUNT, getPassword()));
		setName(System.getProperty(PROP_CLOUDANT_DATABASE, getName()));
		logger.info(toString());
		getDatabase();
	}
	
	protected void initFromResource() {
		InputStream stream = null;
		URL resourceURL = CloudantDatabase.class.getResource("/"+properties);

		if(resourceURL==null) {
			logger.warning("No resource  /"+properties);
			return;
		}
		
		try {
			stream = resourceURL.openStream();
			init(stream);
		} catch(Exception e) {
			logger.warning(e.toString());
		} finally {
			try {stream.close();} catch(Exception e) {}
		}
	}
	
	protected void initFromFile() {
		InputStream stream = null;
		File file = new File(properties);
		if(! file.exists()) {
			logger.fine("File "+properties+" does not exist");
			return;
		}
		if(! file.canRead()) {
			logger.severe("File "+properties+" is not readable");
			return;
		}
		try {
			stream = new FileInputStream(file);
			init(stream);
		} catch(Exception e) {
			logger.severe(e.toString());
		} finally {
			try {stream.close();} catch(Exception e) {}
		}
	}
	
	protected void init(InputStream stream) {
		Properties properties = new Properties();
		try {
			properties.load(stream);
			account = properties.getProperty("CLOUDANT_ACCOUNT",account);
			username = properties.getProperty("CLOUDANT_USERNAME",account);
			password = properties.getProperty("CLOUDANT_PASSWORD",password);
			name = properties.getProperty("CLOUDANT_DATABASE",name);
		} catch(Exception e) {
			logger.warning(e.toString());
		} finally {
			try {stream.close();} catch(Exception e) {}
		}
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
