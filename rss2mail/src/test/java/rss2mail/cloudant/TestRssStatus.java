package rss2mail.cloudant;

import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.junit.Test;

import cloudant.utilities.CloudantDatabase;

public class TestRssStatus {

	public static Logger logger = 
			java.util.logging.Logger.getLogger(BaseRssStatus.class.getCanonicalName());

	@Test
	public void test() {
		try {
			CloudantDatabase database = new CloudantDatabase();

			BaseRssStatus object = (BaseRssStatus)database.find(BaseRssStatus.class.getCanonicalName());
			assertNotNull("RssStatus is null!", object);
			logger.info(object.toString());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
