package rss2mail;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.JsonObject;

import rss2mail.interfaces.RssItem;
import rss2mail.interfaces.SaxHandler;
import rss2mail.interfaces.Sender;

public class RssToMail {
	
	public static Logger logger = 
			java.util.logging.Logger.getLogger(RssToMail.class.getCanonicalName());
	
	private static JsonObject jsonLogs = new JsonObject();
	
	public static void logJson(String sequenceNumber,JsonObject jsonObject) {
		jsonLogs.add(sequenceNumber, jsonObject);
	}
		
	public final static String ARG_SAXHANDLER_CLASS = "rss2mail.saxhandler.class";
	public final static String ARG_SENDER_CLASS = "rss2mail.sender.class";
	public final static String ARG_PROPERTIES = "rss2mail.properties";
	
	public static String PROPERTIES_ID = System.getProperty(ARG_PROPERTIES);
	private static String SAXHANDLER_CLASS = System.getProperty(ARG_SAXHANDLER_CLASS,rss2mail.BaseSaxHandler.class.getCanonicalName());
	private static String SENDER_CLASS = System.getProperty(ARG_SENDER_CLASS,BaseMailSender.class.getCanonicalName());

	private static SaxHandler SAXHANDLER;
	private static Sender SENDER;
			
	public static void main(String[] args) {
		run();
	}
	
	public static JsonObject main(JsonObject args) {
		run();
		return jsonLogs;
	}

	public static void run() {
		
		ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader)cl).getURLs();

        for(URL url: urls){
        	logger.info("IN CLASSPATH ... "+url.getFile());
        }
        
		logger.info(ARG_PROPERTIES+" = "+PROPERTIES_ID);
		logger.info(ARG_SAXHANDLER_CLASS+" = "+SAXHANDLER_CLASS);
		logger.info(ARG_SENDER_CLASS+" = "+SENDER_CLASS);
		try {
			
			SAXHANDLER = getSaxHandler();
			List<RssItem> rssItems = SAXHANDLER.parse();
			
			SENDER = getSenderInterface();
			SENDER.send(rssItems);
			
			SAXHANDLER.commit();
			
			logger.info("Finish SUCCESS");
			
		} catch(Exception e) {
			logger.severe(e.toString());
			e.printStackTrace();
		}
    }
	
    public static SaxHandler getSaxHandler() throws Exception {
    	if(SAXHANDLER!=null) return SAXHANDLER;
		try {
			String classname = SAXHANDLER_CLASS;
			if(classname==null) throw new Exception("SaxHanlderClass is null");
			Class<SaxHandler> clazz = (Class<SaxHandler>)Class.forName(classname);
			Constructor<SaxHandler> constructor = clazz.getConstructor();
			SaxHandler saxHandler = constructor.newInstance();
			return saxHandler;
		} catch(Exception e) {
    		logger.severe(e.toString());
    		throw e;
    	}
    }
    
    public static Sender getSenderInterface() throws Exception {
    	try {
    		String classname = SENDER_CLASS;
    		Class<Sender> clazz = (Class<Sender>)Class.forName(classname); 
    		Constructor<Sender> constructor = clazz.getConstructor();
    		return (Sender)constructor.newInstance(); 
    	} catch(Exception e) {
    		logger.severe(e.toString());
    		throw e;
    	}
    }
}
