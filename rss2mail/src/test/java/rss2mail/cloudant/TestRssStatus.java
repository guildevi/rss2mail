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

			String _id = BaseRssStatus.class.getCanonicalName();
			if(!database.contains(_id)) {
				logger.info(String.format("Document %s does not exists", _id));
				BaseRssStatus baseRssStatus = new BaseRssStatus();
				database.save(baseRssStatus);
			}
			BaseRssStatus object = (BaseRssStatus)database.find(_id);
			assertNotNull("RssStatus is null!", object);
			logger.info(object.toString());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
