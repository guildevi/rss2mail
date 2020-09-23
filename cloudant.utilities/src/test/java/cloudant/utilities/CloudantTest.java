package cloudant.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CloudantTest {
	
	private Logger logger = Logger.getLogger(CloudantTest.class.getCanonicalName());
	private String _id = this.getClass().getSimpleName();
	private CloudantDatabase database = null;
	private CloudantProperties cloudantProperties = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader)cl).getURLs();

        for(URL url: urls){
        	System.out.println(url.getFile());
        }
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

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
		assertNull("cloudantProperties is not null!", cloudantProperties);

		try {
			if(cloudantProperties!=null) {
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
			logger.info(cloudantProperties.toString());
			database.save(cloudantProperties);
			cloudantProperties = null;
		
			cloudantProperties = CloudantProperties.find(database,_id);
			assertNotNull("cloudantProperties is null!", cloudantProperties);
			assertEquals("cloudantProperties has not only one property", 1, cloudantProperties.getProperties().size(), 0);
			cloudantProperties.getProperties().add(new Property("NAME2", "VALUE TWO"));
			logger.info(cloudantProperties.toString());
			database.update(cloudantProperties);
			cloudantProperties = null;
			
			cloudantProperties = CloudantProperties.find(database,_id);
			assertNotNull("cloudantProperties is null!", cloudantProperties);
			assertEquals("cloudantProperties does not have two properties", 2, cloudantProperties.getProperties().size(), 0);
			cloudantProperties = null;
		} catch(Exception e) {
			logger.severe(e.toString());
		}
		
		try {
			database.remove(cloudantProperties);
			cloudantProperties = CloudantProperties.find(database,_id);	
			
		} catch(Exception e) {
			assertNull("cloudantProperties is not null!", cloudantProperties);
		}
	}

}
