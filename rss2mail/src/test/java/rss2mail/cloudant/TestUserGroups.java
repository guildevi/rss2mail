package rss2mail.cloudant;

import static org.junit.Assert.*;

import org.junit.Test;

import cloudant.utilities.CloudantDatabase;

public class TestUserGroups {

	private static java.util.logging.Logger logger = 
			java.util.logging.Logger.getLogger(TestUserGroups.class.getCanonicalName());

	@Test
	public void test() {
		try {
			CloudantDatabase database = new CloudantDatabase();
			BaseUserGroups groups = (BaseUserGroups)database.find(BaseUserGroups.class);
			assertNotNull("UserGroups is null!", groups);
			logger.info(groups.toString());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
