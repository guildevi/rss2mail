package cloudant.utilities;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Properties;
import java.util.logging.Logger;


public class CloudantTest {
	
	public static void main(String[] args) {
		CloudantTest test = new CloudantTest();
		test.test();
	}

	private Logger logger = Logger.getLogger(CloudantTest.class.getCanonicalName());
	private String _id = this.getClass().getSimpleName();
	private CloudantDatabase database = null;
	private CloudantProperties cloudantProperties = null;

	@Test
	public void test() {
		
		try {
			database = new CloudantDatabase();
			logger.info("Connected Cloudant database "+database.getName());
		} catch(Exception e) {
			logger.severe("Failed connect to Cloudant database: "+e.toString());
		}
		assertNotNull("Database is null!", database);
		
		try {
			cloudantProperties = CloudantProperties.find(database,_id);
			logger.info("Found object "+cloudantProperties.toString());
		} catch(Exception e) {
			logger.info("Object "+_id+" does not exists");
		}
		//assertNull("cloudantProperties is not null!", cloudantProperties);

		try {
			if(cloudantProperties!=null) {
				logger.warning("remove "+cloudantProperties.toJson());
				database.remove(cloudantProperties);
				cloudantProperties = null;
			}
		} catch(Exception e) {
			logger.severe("Failed to delete Object "+_id);
		}
		assertNull("cloudantProperties is not null!", cloudantProperties);

		try {
			cloudantProperties = new CloudantProperties(_id);
			Properties properties = new Properties();
			properties.put("NAME1", "VALUE ONE");
			cloudantProperties.setProperties(properties);
			logger.info(String.format("Save %s", cloudantProperties.toString()));
			database.save(cloudantProperties);
			cloudantProperties = null;
		
			cloudantProperties = CloudantProperties.find(database,_id);
			assertNotNull("cloudantProperties is null!", cloudantProperties);
			assertEquals("cloudantProperties has not only one property", 1, cloudantProperties.getProperties().size(), 0);
			cloudantProperties.getProperties().add(new Property("NAME2", "VALUE TWO"));
			logger.info(String.format("Update %s", cloudantProperties.toString()));
			database.update(cloudantProperties);
			cloudantProperties = null;
			
			logger.info(String.format("Find %s", _id));
			cloudantProperties = CloudantProperties.find(database,_id);
			logger.info(String.format("Found %s", cloudantProperties.toJson()));
			assertNotNull("cloudantProperties is null!", cloudantProperties);
			assertEquals("cloudantProperties does not have two properties", 2, cloudantProperties.getProperties().size(), 0);
			cloudantProperties = null;
		} catch(Exception e) {
			logger.severe(e.toString());
		}
		
		try {
			logger.info(String.format("Remove %s", cloudantProperties.get_id()));
			database.remove(cloudantProperties);
			logger.finest(String.format("Find %s", _id));
			cloudantProperties = CloudantProperties.find(database,_id);	
			
		} catch(Exception e) {
			logger.finest(String.format("Exception %s", e.toString()));
			assertNull("cloudantProperties is not null!", cloudantProperties);
		}
	}

}
