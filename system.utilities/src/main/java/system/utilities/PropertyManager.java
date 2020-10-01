/**
* The PropertyManager class is a utility class to obtain property value 
* following the below priority list from:
* 1- The Java system property (-D{key})
* 2- The file named after property filenameKey (-D{filenameKey}) 
* 3- The equivalent environment variable (upper case key and . converted to _)
* 4- The file named after the equivalent environment variable to property filenameKey
*
* @author  Guillaume Devillers
* @version 1.0
* @since   2020.8.20 
*/
package system.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyManager {
	
	public static Logger logger = 
			java.util.logging.Logger.getLogger(PropertyManager.class.getCanonicalName());
	
	public static Properties resources ;
	public static Properties javaProperties ;
	public static Properties environmentVariables ;
	
	/* Environment variable corresponding to property key
	   are all upper case and underscore (_) replaces dot (.) */
	private static String keyToEnv(String key) {
		return key.toUpperCase().replace('.', '_');
	}
	
	public static String getProperty(String filenameKey, String key) {
		return getProperty(filenameKey, key, null);
	}	
	
	public static String getProperty(String filenameKey, String key, String def) {
			
		logger.finest(String.format("getProperty(%s,%s,%s)",filenameKey,key,def));
		String value = System.getProperty(key);
		if(value!=null) {
			logger.finest(String.format("Java Property %s is %s",key,value));
			return value;
		} else {
			logger.finest(String.format("Java Property %s not set",key));
		}
		
		if(javaProperties==null) loadJavaProperties(filenameKey);
		value = javaProperties.getProperty(key); 
		if(value!=null)	{
			logger.finest(String.format("%s = %s in Java Property file %s",key,value,filenameKey));
			return value;
		} else {
			logger.finest(String.format("%s is not set in Java Property file %s",key,filenameKey));
		}
		
		String env = keyToEnv(key);
		value = System.getenv(env);
		if(value!=null) {
			logger.finest(String.format("Environment variable %s is %s",env,value));
			return value;
		} else {
			logger.finest(String.format("Environment variable %s not set",env));
		}
		
		if(environmentVariables==null) loadEnvironmentVariables(filenameKey);
		value = environmentVariables.getProperty(key);
		if(value!=null) {
			logger.finest(String.format("%s = %s in environment property file %s",key,value,filenameKey));
			return value;
		} else {
			logger.finest(String.format("%s is not set in environment property file %s",key,filenameKey));
		}
		
		if(resources==null) loadResources(filenameKey);
		value = resources.getProperty(key);
		if(value!=null) {
			logger.finest(String.format("%s = %s in resource %s",key,value,filenameKey));
			return value;
		} else {
			logger.finest(String.format("%s is not set in resource %s",key,filenameKey));
		}
		
		return def; 
	}
	
	private static void loadResources(String filenameKey) {
		resources = new Properties();
		InputStream stream = null;
		URL resourceURL = PropertyManager.class.getResource("/"+filenameKey);
		
		if(resourceURL==null) {
			logger.warning("No resource  /"+filenameKey);
			return;
		}
		
		try {
			stream = resourceURL.openStream();
			resources.load(stream);
		} catch(Exception e) {
			logger.warning(e.toString());
		} finally {
			try {stream.close();} catch(Exception e) {}
		}
	}

	private static void loadJavaProperties(String filenameKey) {
		/*if(logger.isLoggable(Level.FINEST)) {
			Enumeration<Object> keys = System.getProperties().keys();
			while(keys.hasMoreElements()) {
				String key = (String)keys.nextElement();
				String value = System.getProperty(key); 
				logger.finest(String.format("Java Property %s = %s", key, value));
			}
		}*/
		javaProperties = new Properties();
		String filename = System.getProperty(filenameKey);
		logger.finest(String.format("Java Property %s is %s", filenameKey, filename));
		if(filename!=null) {
			loadProperties(javaProperties, new File(filename));
			if(logger.isLoggable(Level.FINEST)) {
				Enumeration<Object> e = javaProperties.keys();
				while(e.hasMoreElements()) {
					String key = (String)e.nextElement();
					String value = javaProperties.getProperty(key);
					logger.finest(String.format("%s is %s in %s", key, value, filename));
				}
			}
		}
	}
	
	private static void loadEnvironmentVariables(String filenameKey) {
		environmentVariables = new Properties();
		String filename = System.getenv(keyToEnv(filenameKey));
		logger.finest(String.format("Environment property %s is %s", filenameKey, filename));
		if(filename!=null) {
			loadProperties(environmentVariables, new File(filename));
			if(logger.isLoggable(Level.FINEST)) {
				Enumeration<Object> e = environmentVariables.keys();
				while(e.hasMoreElements()) {
					String key = (String)e.nextElement();
					String value = environmentVariables.getProperty(key);
					logger.finest(String.format("%s is %s in %s", key, value, filename));
				}
			}
		}
	}
	
	private static void loadProperties(Properties properties, File file) {
		InputStream stream = null;
		if(! file.exists()) {
			logger.warning(String.format("File %s does not exist",file.getName()));
		} else if(! file.canRead()) {
			logger.severe(String.format("File % is not readable",file.getName()));
		} else {
			try {
				stream = new FileInputStream(file);
				properties.load(stream);
			} catch(Exception e) {
				logger.severe(e.toString());
			} finally {
				try {stream.close();} catch(Exception e) {}
			}
		}
	}

}
