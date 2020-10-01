package rss2mail.cloudant;

import static org.junit.Assert.*;

//import java.util.Properties;
import java.util.logging.Logger;

import org.junit.Test;

import cloudant.utilities.CloudantDatabase;

public class TestServerProperties {
	
	public static Logger logger = 
			java.util.logging.Logger.getLogger(TestServerProperties.class.getCanonicalName());

	@Test
	public void test() {
		try {
			CloudantDatabase database = new CloudantDatabase(); 
			BaseServerProperties senderProperties = 
					(BaseServerProperties)database.find(BaseServerProperties.class,rss2mail.RssToMail.DOCUMENT_ID);
			//Properties properties = senderProperties.toProperties();
			assertNotNull("Sender properties does not exist", senderProperties);
			logger.info(senderProperties.toString());
			assertNotNull("Sender properties host is not set", senderProperties.getHost());
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

}
