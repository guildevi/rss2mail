package rss2mail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import rss2mail.cloudant.BaseProperties;
import rss2mail.interfaces.Database;
import rss2mail.interfaces.RssItem;
import rss2mail.interfaces.RssStatus;
import rss2mail.interfaces.SaxHandler;
import rss2mail.interfaces.SaxHandlerProperties;
import system.utilities.PropertyManager;

public class BaseSaxHandler extends DefaultHandler implements SaxHandler 
{	
	public static Logger logger = 
			Logger.getLogger(BaseSaxHandler.class.getCanonicalName());

	private final static String SYSPROP_PROPERTIES = System.getProperty("rss2mail.saxhandler.properties",RssToMail.SYSPROP_PROPERTIES);
	private final static String SYSPROP_DATABASE_CLASS = "rss2mail.saxhandler.database.class";
	private final static String SYSPROP_DATABASE_NAME = "rss2mail.saxhandler.database.name";
	private final static String SYSPROP_DOCUMENT_ID = "rss2mail.saxhandler.document.id";
	private final static String SYSPROP_PROPERTIES_CLASS = "rss2mail.saxhandler.properties.class";
	
	private static String databaseClass;
	private static String databaseName;
	private static String documentId;
	private static String propertiesClass;
		
	private Database database;
	private SaxHandlerProperties properties;
    private RssStatus rssStatus;

	protected final static int INAME_UNKNOWN=-10;
	protected final static int INAME_ROOT=0;
	protected final static int INAME_RSS=10;
	protected final static int INAME_CHANNEL=20;
	protected final static int INAME_ITEM=30;
	protected final static int INAME_PUBDATE=40;
	protected final static int INAME_TITLE=50;
	protected final static int INAME_LINK=60;
	protected final static int INAME_GUID=70;
	protected final static int INAME_DESCRIPTION=80;
	
	protected final static int KEEP_ALL=0;
	protected final static int KEEP_NEW=1;
	protected final static int KEEP_LATEST=2;
		
	private URL url;
	private Class<RssItem> rssItemClass; 
	private SimpleDateFormat pubDateFormat;
    
	private int keep=KEEP_ALL;
    //private Date laterThan = null;
    private Date latest = new Date(0);
    //private SimpleDateFormat pubDateFormat;

	protected String[] qNamesTree = new String[10];
	protected int[] iNamesTree = new int[10];
	protected int treeLevel=0;
	
    private List<RssItem> rssItems = new ArrayList<RssItem>();
    private RssItem rssItem;

	public BaseSaxHandler() throws Exception {
		super();
		databaseClass = 
				PropertyManager.getProperty(SYSPROP_PROPERTIES,SYSPROP_DATABASE_CLASS,RssToMail.DATABASE_CLASS);
		databaseName = 
				PropertyManager.getProperty(SYSPROP_PROPERTIES,SYSPROP_DATABASE_NAME,RssToMail.DATABASE_NAME);
		documentId = 
				PropertyManager.getProperty(SYSPROP_PROPERTIES,SYSPROP_DOCUMENT_ID,RssToMail.DOCUMENT_ID);
		propertiesClass = 
				PropertyManager.getProperty(SYSPROP_PROPERTIES,SYSPROP_PROPERTIES_CLASS,BaseProperties.class.getCanonicalName());
			
		initDatabase();
		initProperties();
		logger.info(getProperties().toString());
		initRssStatus();
		logger.info(getRssStatus().toString());
		
		setURL(new URL(getProperties().getUrl()));
		setRssItemClass((Class<RssItem>)Class.forName(getProperties().getItemClass()));
		setPubDateFormat(new SimpleDateFormat(getProperties().getPubDateFormat()));
	}

	public void setDatabase(Database database) {
		this.database = database;
	}
		
	public void setProperties(SaxHandlerProperties properties) {
		this.properties = properties;
	}
		
	public void setURL(URL url) {
		this.url = url;
	}
	
	public URL getURL() {
		return url;
	}
	
	public void setPubDateFormat(SimpleDateFormat format) {
		this.pubDateFormat = format;
	}
	
	public SimpleDateFormat getPubDateFormat() {
		return pubDateFormat;
	}
	
	public List<RssItem> parse() 
	throws Exception {
		
		if(getRssStatus().getTimestamp()==null) {
			keep = KEEP_LATEST;
		} else {
			keep = KEEP_NEW;
		}
		
		try {
			InputStream stream = getURL().openStream();

			int max = 1024*1024;
			byte[] bytes = new byte[max];
			int read = 0;
			int size = 0;
			
			while((read = stream.read(bytes,size,max-size))>=0) {
				size=size+read;
			}
		
			if(size==getRssStatus().getSize()) {
				logger.info("Feed size is "+size+" => unmodified...");
				return getRssItems();
			} else {
				logger.info("Feed size is "+size+" => modified...");
				getRssStatus().setSize(size);
			}
			
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
	
			SaxHandler handler = this;
			saxParser.parse(new ByteArrayInputStream(bytes,0,size),(DefaultHandler)handler);
			
			rssStatus.setTimestamp(getLatest());
			
			return getRssItems();
			
		} catch(Exception e) {
			logger.severe(e.toString());
			throw e;
		}
	}

    //getter method for employee list
    public List<RssItem> getRssItems() {
        return rssItems;
    }

    
    public Date getLatest() {
		return latest;
    }
    
    public void clearItems() {
    		rssItems.clear();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
    throws SAXException {
		logger.fine("uri="+uri+", localName="+localName+", qNAme="+qName);
		treeLevel++;
		qNamesTree[treeLevel]=qName;
		iNamesTree[treeLevel]=parseQName(qName);
		if(iNamesTree[treeLevel]==INAME_ITEM) {
			startElementItem();
		}
    }
 
    public void startElementItem()
    throws SAXException {
		try {
			rssItem = (RssItem)getRssItemClass().newInstance();
			rssItem.setPubDateFormat(getPubDateFormat());
		} catch(IllegalAccessException e) {
			throw new SAXException(e);
		} catch(InstantiationException e) {
			throw new SAXException(e);
		} catch(ClassCastException e) {
			throw new SAXException(e);
		}
    }
    
    public int parseQName(String qName) {
    		int iName=INAME_UNKNOWN;
		if (qName.equalsIgnoreCase("rss")) {
            iName=INAME_RSS;
        } else if (qName.equalsIgnoreCase("channel")) {
        	iName=INAME_CHANNEL;
        } else if (qName.equalsIgnoreCase("item")) {
        		iName=INAME_ITEM;
        } else if(qName.equalsIgnoreCase("pubDate")) {
    			iName=INAME_PUBDATE;
        } else if(qName.equalsIgnoreCase("title")) {
    			iName=INAME_TITLE;
        } else if(qName.equalsIgnoreCase("link")) {
        		iName=INAME_LINK;
        } else if(qName.equalsIgnoreCase("guid")) {
        		iName=INAME_GUID;
        } else if(qName.equalsIgnoreCase("description")) {
        		iName=INAME_DESCRIPTION;
        } else {
        		iName=INAME_UNKNOWN;
        }
		return iName;
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
		if(iNamesTree[treeLevel]==INAME_PUBDATE) {
			rssItem.setPubDate();
		} else if(iNamesTree[treeLevel]==INAME_ITEM) {
			endElementItem();
		}
		treeLevel--;
    }
    
    protected void endElementItem() {
		//Logger.info("Title: ["+getRssItem().getGuid()+"] "+getRssItem().getTitle());
		//Logger.info("Publication date: "+getRssItem().getPubDate());
    		Date itemDate = rssItem.getPubDate();
    		if(itemDate.after(latest)) {
    			setLatest(itemDate);
    			if(keep==KEEP_LATEST) {
    				if(rssItems.size()==0) rssItems.add(rssItem);
    				else rssItems.set(0, rssItem);
    				logger.info(latest.toString()+" - "+itemDate.toString()+" - "+rssItem.getTitle());
    			}
    		}  
    		
    		if (keep==KEEP_NEW && itemDate.after(getRssStatus().getTimestamp())) {
			rssItems.add(rssItem);
			logger.info(getRssStatus().getTimestamp()+" - "+latest.toString()+" - "+itemDate.toString()+" - "+rssItem.getTitle());
		}
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
    		if(iNamesTree[treeLevel-1]!=INAME_ITEM) return;
    		setItemAttribute(ch, start, length);
    }
    
    public void setItemAttribute(char ch[], int start, int length) {
    		switch (iNamesTree[treeLevel]) {
    			case INAME_PUBDATE:
    				rssItem.appendToPubDate(getData(ch,start,length));
    				break;
    			case INAME_TITLE:
    				rssItem.appendToTitle(getData(ch,start,length));
    				break;
    			case INAME_LINK:
    				rssItem.appendToLink(getData(ch,start,length));
    				break;
    			case INAME_GUID:
    				rssItem.appendToGuid(getData(ch,start,length));
    				break;
    			case INAME_DESCRIPTION:
    				rssItem.appendToDescription(getData(ch,start,length));
    				break;
    			default:
    				break;
		}
    }

    protected String getData(char ch[], int start, int length) {
    		String data = new String(ch,start,length);
		logger.fine("characters="+data);
		return data;
    }
    
	public Class<RssItem> getRssItemClass() {
		return rssItemClass;
	}

	public void setRssItemClass(Class<RssItem> rssItemClass) {
		this.rssItemClass = rssItemClass;
	}
	/*
	protected void setLaterThan(Date laterThan) {
		this.laterThan = laterThan;
	}
	*/
	protected void setLatest(Date latest) {
		this.latest = latest;
	}

	protected RssItem getRssItem() {
		return rssItem;
	}

	protected void setRssItem(RssItem item) {
		this.rssItem = item;
	}
	
	protected boolean keepAll() {
		return keep == KEEP_ALL;
	}
	
	protected boolean keepLatest() {
		return keep == KEEP_LATEST;
	}
	
	protected boolean keepNew() {
		return keep == KEEP_NEW;
	}

	protected void setRssStatus(RssStatus status) {
		this.rssStatus = status;
	}

	protected void setRssItems(List<RssItem> rssItems) {
		this.rssItems = rssItems;
	}
	
	protected void addToRssItems(RssItem rssItem) {
		if(!getRssItems().contains(rssItem)) {
			getRssItems().add(rssItem);
		}
	}
	
	private Database getDatabase() {
		return database;
    }
	
	private void initDatabase() throws Exception {
		try {
    		Class<?> clazz = Class.forName(databaseClass);
    		if(databaseName == null) {
    			setDatabase((Database)clazz.getConstructor().newInstance());
    		} else {
    			setDatabase((Database)clazz.getConstructor(String.class).newInstance(databaseName));
    		}
       	} catch(Exception e) {
    		logger.severe(e.toString());
    		throw e;
    	}
    }
	
	public SaxHandlerProperties getProperties() {
		return properties;
    }
	
	public void initProperties() throws Exception {
		try {
    		setProperties((SaxHandlerProperties)getDatabase().find(propertiesClass, documentId));
       	} catch(Exception e) {
    		logger.severe(e.toString());
    		throw e;
    	}
    }
	
	public RssStatus getRssStatus() {
		return rssStatus;
    }	
	
	public void initRssStatus() throws Exception {
		try {
    		String classname = getProperties().getStatusClass();
    		String _id = getProperties().getStatusDocId();
    		if(_id == null) _id = classname;
    		if(!getDatabase().contains(_id)) _id=documentId; 
    		setRssStatus((RssStatus)getDatabase().find(classname, _id));
    	} catch(Exception e) {
    		logger.severe(e.toString());
    		throw e;
    	}
    }
	
	public void commit() throws Exception {
		getDatabase().update(getRssStatus());
	}
}