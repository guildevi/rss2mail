package gson.logger;

import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;

public class GsonHandlerTest {
	
	private static JsonObject jsonLogs = new JsonObject();
		
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Logger logger = java.util.logging.Logger.getLogger(GsonHandlerTest.class.getCanonicalName());
		logger.severe("SEVERE TEST");
		logger.warning("WARN TEST");
		logger.info("INFO TEST");
		logger.fine("FINE TEST");
		logger.finest("FINEST TEST");
		System.out.println(getJsonLogs().toString());
		System.out.println(GsonHandler.getJsonLogs().toString());
	}
	
	public static void logJson(String sequenceNumber,JsonObject jsonObject) {
		getJsonLogs().add(sequenceNumber, jsonObject);
	}
	
	public static JsonObject getJsonLogs() {
		return jsonLogs;
	}

}
