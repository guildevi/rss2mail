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
import java.util.Properties;
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
		return getProperty(filenameKey, filenameKey, null);
	}	
	
	public static String getProperty(String filenameKey, String key, String def) {
			
		String value = System.getProperty(key);
		if(value!=null) return value;

		if(javaProperties==null) loadJavaProperties(filenameKey);
		value = javaProperties.getProperty(key); 
		if(value!=null) return value;
		
		String env = keyToEnv(key);
		value = System.getenv(env);
		if(value!=null) return value; 
		
		if(environmentVariables==null) loadEnvironmentVariables(filenameKey);
		value = environmentVariables.getProperty(key);
		if(value!=null) return value;
		
		if(resources==null) loadResources(filenameKey);
		value = resources.getProperty(key);
		if(value!=null) return value;
		
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
		javaProperties = new Properties();
		String filename = System.getProperty(filenameKey);
		if(filename!=null) {
			loadProperties(javaProperties, new File(filename));
		}
	}
	
	private static void loadEnvironmentVariables(String filenameKey) {
		environmentVariables = new Properties();
		String filename = System.getenv(keyToEnv(filenameKey));
		if(filename!=null) {
			loadProperties(environmentVariables, new File(filename));
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
