package rss2mail.cloudant;

import static org.junit.Assert.*;

import java.util.Properties;
import java.util.logging.Logger;

import org.junit.Test;

import cloudant.utilities.CloudantDatabase;
import cloudant.utilities.exceptions.CloudantException;

public class TestMailProperties {
	
	public static Logger logger = 
			java.util.logging.Logger.getLogger(TestMailProperties.class.getCanonicalName());

	@Test
	public void test() {
		try {
			CloudantDatabase database = new CloudantDatabase();
			BaseMailProperties senderProperties = (BaseMailProperties)database.find(BaseMailProperties.class);
			Properties properties = senderProperties.toPropeties();
			assertNotNull("Sender properties does not exist", senderProperties);
			logger.info(senderProperties.toString());
			assertNotNull("Sender properties host is not set", senderProperties.getHost());
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

}
