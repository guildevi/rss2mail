package gson.logger;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import com.google.gson.JsonObject;

public class GsonHandler extends Handler {
	
	private static java.util.logging.Logger logger = 
			java.util.logging.Logger.getLogger(GsonHandler.class.getCanonicalName());
	
	public static String methodProperty = GsonHandler.class.getCanonicalName()+".method";
	public static String methodName = GsonHandler.class.getCanonicalName()+".logJson";
	public static DecimalFormat sequenceNumberFormat = new DecimalFormat("00000000");
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
		
	private static Method METHOD=initMethod();
	private static JsonObject JSONOBJECT = new JsonObject();

	@Override
	public void publish(LogRecord record) {
		if(!this.isLoggable(record)) return;
		if(METHOD==null) return;
		JsonObject jsonRecord = new JsonObject();
		jsonRecord.addProperty("timestamp", dateFormat.format(new Date(record.getMillis())));
		jsonRecord.addProperty("level", record.getLevel().getLocalizedName());
		jsonRecord.addProperty("class", record.getClass().getCanonicalName());
		jsonRecord.addProperty("method",record.getSourceMethodName());
			
		if(record.getMessage()!=null) jsonRecord.addProperty("message", record.getMessage());
		if(record.getThrown()!=null) {
			jsonRecord.addProperty("exception", record.getThrown().getStackTrace().toString());
		}
					
		String sequence = sequenceNumberFormat.format(record.getSequenceNumber());
		try {
			METHOD.invoke(null, sequence, jsonRecord);
		} catch(Exception e) {
			logger.severe(e.toString());
		}
	}

	@Override
	public void flush() {
		//
	}

	@Override
	public void close() throws SecurityException {

	}
	
	private static Method initMethod() {
		logger.finest(logger.getName()+".level = "+logger.getLevel());
		logger.finest("Init method in "+GsonHandler.class.getCanonicalName());
		LogManager manager = LogManager.getLogManager();
        String handlerClassName = GsonHandler.class.getCanonicalName();    
        String methodName = manager.getProperty(handlerClassName+".method");
        if(methodName==null)  methodName = GsonHandler.methodName;       
        String className = methodName.substring(0,methodName.lastIndexOf('.'));
        methodName = methodName.substring(methodName.lastIndexOf('.')+1);
        Method method = null;
        try {
        	Class classObject = Class.forName(className);
        	method = classObject.getMethod(methodName, String.class, JsonObject.class);
        	logger.finest("Init with method = "+className+"."+methodName);
        } catch(Exception e) {
        	logger.severe(e.toString());
        }
        return method;
	}
	
	public static void logJson(String sequenceNumber,JsonObject jsonObject) {
		getJsonLogs().add(sequenceNumber, jsonObject);
	}
	
	public static JsonObject getJsonLogs() {
		return JSONOBJECT;
	}

}
