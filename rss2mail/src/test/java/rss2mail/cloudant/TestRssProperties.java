package rss2mail.cloudant;

import static org.junit.Assert.*;

import org.junit.Test;

import cloudant.utilities.CloudantDatabase;

public class TestRssProperties {

	private static java.util.logging.Logger logger = 
	java.util.logging.Logger.getLogger(TestRssProperties.class.getCanonicalName());

	@Test
	public void test() {
		try {
			CloudantDatabase database = new CloudantDatabase();
			BaseProperties rssProperties = (BaseProperties)database.find(BaseProperties.class,"BaseRssToMail");
			assertNotNull("RssProperties is null!", rssProperties);
			logger.info(rssProperties.toString());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
