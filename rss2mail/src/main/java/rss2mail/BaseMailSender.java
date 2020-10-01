package rss2mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import rss2mail.cloudant.BaseProperties;
import rss2mail.cloudant.BaseUser;
import rss2mail.exceptions.RssException;
import rss2mail.interfaces.Database;
import rss2mail.interfaces.ServerProperties;
import rss2mail.interfaces.RssItem;
import rss2mail.interfaces.Sender;
import rss2mail.interfaces.SenderProperties;
import rss2mail.interfaces.UserGroup;
import rss2mail.interfaces.UserGroups;
import system.utilities.PropertyManager;

public class BaseMailSender implements Sender
{	
	public static Logger logger = 
			java.util.logging.Logger.getLogger(BaseMailSender.class.getCanonicalName());

	private final static String SYSPROP_PROPERTIES = System.getProperty("rss2mail.mailsender.properties",RssToMail.SYSPROP_PROPERTIES);
	private final static String SYSPROP_DATABASE_CLASS = "rss2mail.mailsender.database.class";
	private final static String SYSPROP_DATABASE_NAME = "rss2mail.mailsender.database.name";
	private final static String SYSPROP_DOCUMENT_ID = "rss2mail.mailsender.document.id";
	private final static String SYSPROP_PROPERTIES_CLASS = "rss2mail.sender.properties.class";
	
	private String databaseClass;
	private String databaseName;
	private String documentId;
	private String propertiesClass;
		
	private Database database;
	private SenderProperties properties;	
	private ServerProperties serverProperties;	
	private UserGroups userGroups;
	
	public BaseMailSender() throws Exception {
		
		super();
		databaseClass = 
				PropertyManager.getProperty(SYSPROP_PROPERTIES,SYSPROP_DATABASE_CLASS,RssToMail.DATABASE_CLASS);
		databaseName = 
				PropertyManager.getProperty(SYSPROP_PROPERTIES,SYSPROP_DATABASE_NAME,RssToMail.DATABASE_NAME);
		propertiesClass = 
				PropertyManager.getProperty(SYSPROP_PROPERTIES,SYSPROP_PROPERTIES_CLASS,BaseProperties.class.getCanonicalName());
		documentId = 
				PropertyManager.getProperty(SYSPROP_PROPERTIES,SYSPROP_DOCUMENT_ID,RssToMail.DOCUMENT_ID);

		initDatabase();
		initProperties();
		initUserGroups();
		initServerProperties();
	}
			
	public void send(List<RssItem> rssItems) throws Exception {
		send(rssItems,getUserGroups());
	}
	
	public void send(List<RssItem> rssItems, UserGroups userGroups) throws Exception {
		try {
			for(RssItem item : rssItems) {
				logger.info(item.getPubDate()+"\t"+item.getTitle());
				send(item);
			}			
		} catch(Exception e) {
			logger.severe(e.toString());
			throw e;
		} 
	}

	public void send(RssItem rssItem)  throws Exception {		
	
		Session session = null;
		
		try {
			session = Session.getInstance(getServerProperties().toProperties());
		} catch(Exception e) {
			logger.severe(e.toString());
			throw e;
		}
		
		try {
			Transport transport = session.getTransport("smtp");
			transport.connect(getServerProperties().getUser(), getServerProperties().getPassword());
		} catch(Exception e) {
			logger.severe(e.toString());
			throw e;
		}
		
		try {
			rssItem.init();
			Iterator<UserGroup> iterator = userGroups.iterator();
			while(iterator.hasNext()) {
				UserGroup userGroup = iterator.next();
				send(session,userGroup,rssItem);
			} 
		} catch(Exception e) {
			logger.severe(e.toString());
			throw e;
		}
	}
		
	private void send(Session session, UserGroup userGroup, RssItem item) throws Exception  {
		try {
			if(!filter(userGroup,item)) 
			{
				MimeMessage message = getMessage(session,userGroup,item);		
				logger.info("Send to "+userGroup.getName());
				Transport.send(message, serverProperties.getUser(), serverProperties.getPassword());
			}
		} catch(RssException e) {
			logger.warning(e.getLocalizedMessage());
		} catch(MessagingException e) {
			logger.warning(e.getLocalizedMessage());				
		}		
	}
	
	public boolean filter(UserGroup userGroup, RssItem item) throws RssException {
		return false;
	}
	
	protected MimeMessage getMessage(Session session, UserGroup userGroup, RssItem item)
	throws Exception {	
		
		try {
			SimpleDateFormat dateFormat=new SimpleDateFormat(userGroup.getDateformat());
			dateFormat.setTimeZone(TimeZone.getTimeZone(userGroup.getTimezone()));
		
			InternetAddress[] addresses=new InternetAddress[userGroup.getUsers().size()];
			for(int i=0; i<userGroup.getUsers().size(); i++) {
				BaseUser user=userGroup.getUsers().get(i);
				addresses[i] = new InternetAddress(user.getAddress(), user.getPersonal());
			}	
			MimeMessage msg = new MimeMessage(session);
			
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(getServerProperties().getUser());
			InternetAddress[] replyTo=new InternetAddress[1];
			replyTo[0] = new InternetAddress(getServerProperties().getReplyTo());
			msg.setReplyTo(replyTo);
			msg.setSubject(getSubject(userGroup, item), "UTF-8");
			
			StringBuffer buffer=new StringBuffer();
			buffer.append("<p><strong>"+getProperties().getTitle()+": </strong>");
			buffer.append("<a href=\"");
			buffer.append(item.getLink());
			buffer.append("\">");
			buffer.append(item.getTitle());
			buffer.append("</a></p>");

			buffer.append(htmlDate("pubDate",""
					+ "Publication date: ",dateFormat,item.getPubDate()));
		
			buffer.append(getContent(userGroup,item));
	
			msg.setContent(buffer.toString(),"text/HTML");
			msg.setRecipients(Message.RecipientType.BCC, addresses);
		
			return msg;	
		} catch(Exception e) {
			logger.warning(e.toString());
			throw e;
		} 
	}

	public String htmlDate(String id, String name, SimpleDateFormat dateFormat, Date date) {
		return htmlString(id, name, dateFormat.format(date));
	}

	public String htmlString(String id, String name, String value) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("<p>");
		if(name != null) {
			buffer.append("<strong>"+name+":</strong> ");
		}
		if(value!=null) buffer.append(value);
		buffer.append("</p>");
		return buffer.toString();
	}

	public String getSubject(UserGroup userGroup, RssItem item) {
		StringBuffer buffer = new StringBuffer(properties.getTitle());
		buffer.append(": ");
		buffer.append(item.getMailSubject());
		return buffer.toString();	
	}
	
	public String getContent(UserGroup userGroup, RssItem item) {
		return item.getDescription();
	}
	
	private Database getDatabase() {
		return database;
    }
	
	private void setDatabase(Database database) {
		this.database = database;
    }
	
	private void initDatabase() throws Exception {
		try {
    		Class<?> clazz = Class.forName(databaseClass);
    		if(databaseName != null) {
    			setDatabase((Database)clazz.getConstructor(String.class).newInstance(databaseName));
    		} else {
    			setDatabase((Database)clazz.getConstructor().newInstance());
    		}
       	} catch(Exception e) {
    		logger.severe(e.toString());
    		throw e;
    	}
    }
	
	public SenderProperties getProperties() {
		return properties;
    }
	
	public void setProperties(SenderProperties properties) {
		this.properties = properties;
    }
	
	public void initProperties() throws Exception {
		try {
    		setProperties((SenderProperties)getDatabase().find(propertiesClass,documentId));
       	} catch(Exception e) {
    		logger.severe(e.toString());
    		throw e;
    	}
    }
	
	public UserGroups getUserGroups() {
		return userGroups;
    }
	
	public void setUserGroups(UserGroups userGroups) {
		this.userGroups = userGroups;
    }
	
	public void initUserGroups() throws Exception {
		try {
    		String classname = getProperties().getUserGroupsClass();
    		String _id = getProperties().getUserGroupsDocId();
    		if(_id == null) _id = classname;
    		if(!getDatabase().contains(_id)) _id=documentId; 
    		setUserGroups((UserGroups)getDatabase().find(classname, _id));
       	} catch(Exception e) {
    		logger.severe(e.toString());
    		throw e;
    	}
    }
	
	public ServerProperties getServerProperties() {
		return serverProperties;
    }
	
	public void setServerProperties(ServerProperties mailProperties) {
		this.serverProperties = mailProperties;
    }
	
	public void initServerProperties() throws Exception {
		try {
			String classname = getProperties().getServerClass();
    		String _id = getProperties().getServerDocId();
  
    		if(_id == null) _id = classname;
    		if(!getDatabase().contains(_id)) _id=documentId; 
    		
    		setServerProperties((ServerProperties)getDatabase().find(classname, _id));
       	} catch(Exception e) {
    		logger.severe(e.toString());
    		throw e;
    	}
    }
}
