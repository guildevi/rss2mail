package rss2mail.ibmcloud.pup;


import java.util.logging.Logger;

import rss2mail.exceptions.FailedSubstringException;
import rss2mail.exceptions.InvalidIncludeValueException;
import rss2mail.exceptions.PrefixNotFoundException;
import rss2mail.exceptions.SuffixNotFoundException;

public class ContainedData {
	
	public static Logger logger = 
			java.util.logging.Logger.getLogger(ContainedData.class.getCanonicalName());
	
	public final static int INCLUDE_NONE=0;
	public final static int INCLUDE_PREFIX=-1;
	public final static int INCLUDE_SUFFIX=1;
	public final static int INCLUDE_BOTH=2;
	private int start;
	private int end;
	private String prefix;
	private String suffix;
	private String source;

	public static void main(String[] args) {
		String data = "<strong>Category: </strong>platform<p><strong>Regions Affected: </strong>ibm:yp:us-east <p><strong>Event Start Time: </strong>Wed, 25 Jul 2018 03:45:51 GMT<p><strong>Event End Time: </strong>Wed, 25 Jul 2018 06:19:53 GMT<p><strong>Last Update Time: </strong>Wed, 25 Jul 2018 06:39:37 GMT<p><p><strong>Services/Components Affected:</strong></p>\n" + 
				"<ul>\n" + 
				"<li>Cloud Foundry applications</li>\n" + 
				"<li>Command line tooling</li>\n" + 
				"</ul>\n" + 
				"<p>&nbsp;</p>\n" + 
				"<p><strong>Impact:</strong></p>\n" + 
				"<ul>\n" + 
				"<li>Cloud Foundry application management is unavailable (push and restage actions)</li>\n" + 
				"<li>CLI login (using cf-cli or ibmcloud-cli) failures</li>\n" + 
				"</ul>\n" + 
				"<p>&nbsp;</p>\n" + 
				"<p><strong>Status:</strong></p>\n" + 
				"<ul>\n" + 
				"<li><strong>2018-07-25 04:15 UTC</strong> - INVESTIGATING - The operations team is aware of the issues and are currently investigating.</li>\n" + 
				"<li><strong>2018-07-25 06:35 UTC</strong> - RESOLVED - Issues affecting login and app management are now resolved at 06:19 UTC</li>\n" + 
				"</ul><p>]]></content:encoded>\n" + 
				"        </item>\n" + 
				"        <item>\n" + 
				"            <title><![CDATA[RESOLVED: Issues accessing multiple Watson services]]></title>\n" + 
				"            <link>https://console.bluemix.net/status/notification/908f008fef04cfa7cd78441e5c7b8421</link>\n" + 
				"            <guid>908f008fef04cfa7cd78441e5c7b8421</guid>\n" + 
				"            <pubDate>Wed, 25 Jul 2018 15:09:28 GMT</pubDate>\n" + 
				"            <description><![CDATA[<p><strong>Services/Components Affected:</strong></p>\n" + 
				"<ul>\n" + 
				"<li>Watson Discovery</li>\n" + 
				"<li>Watson Speech-to-Text</li>\n" + 
				"<li>Watson Text-to-Speech</li>\n" + 
				"<li>Watson Language Translator</li>\n" + 
				"<li>Watson Natural Language Understanding</li>\n" + 
				"<li>Watson Assistant</li>\n" + 
				"<li>Watson Personality Insights</li>\n" + 
				"<li>Watson Virtual Agent</li>\n" + 
				"</ul>\n" + 
				"<p>&nbsp;</p>\n" + 
				"<p><strong> Impact</strong>:</p>\n" + 
				"<ul>\n" + 
				"<li>Service instances are unaccessible</li>\n" + 
				"</ul>\n" + 
				"<p>&nbsp;</p>\n" + 
				"<p><strong>Status</strong>:</p>\n" + 
				"<ul>\n" + 
				"<li>2018-07-25 15:27 UTC - RESOLVED: The issues have been resolved.</li>\n" + 
				"</ul>";
		try {
		ContainedData cda = new ContainedData(data,"<strong>Regions Affected: </strong>"," <p>");
		logger.info(cda.getSource());
		//ContainedDate cde = new ContainedDate(data,"<strong>Event Start Time: </strong>"," GMT",ContainedDate.INCLUDE_SUFFIX,LabeledDate.DATEFORMAT);
		//Logger.info(cde.getSource());
		//LabeledDate ld = new LabeledDate(data,"Event Start Time"," GMT"); 
		//Logger.info(ld.getSource());
		//Logger.info(ld.getDate().toString());
		int offset = data.indexOf("STATUS");
		StatusDate sd = new StatusDate(data,"2018-"," UTC",offset); 
		logger.info(sd.getSource());
		logger.info(sd.getDate().toString());
		} catch(Exception e) {
			logger.severe(e.getMessage());
		}
	 }
	
	
	public ContainedData() {
	}
	
	public ContainedData(String data, String prefix,String suffix,int offset, int include) 
	throws PrefixNotFoundException, SuffixNotFoundException, InvalidIncludeValueException, FailedSubstringException {
		init(data, prefix, suffix, offset,include);
	}

	public ContainedData(String data, String prefix,String suffix)
	throws PrefixNotFoundException, SuffixNotFoundException, InvalidIncludeValueException, FailedSubstringException {
		init(data, prefix, suffix, 0,ContainedData.INCLUDE_NONE);
	}

	protected void init(String data, String prefix,String suffix,int offset,int include) 
	throws PrefixNotFoundException, SuffixNotFoundException, InvalidIncludeValueException, FailedSubstringException {
		setPrefix(prefix);
		setSuffix(suffix);
		setStart(data.indexOf(prefix,offset));
		if(getStart()==-1) {
			throw new PrefixNotFoundException("Prefix "+prefix+" not found from offset "+offset+" in \n"+data);
		}
		setEnd(data.indexOf(suffix,getStart()+prefix.length()));
		if(getEnd()==-1) {
			throw new SuffixNotFoundException("Suffix "+suffix+"not found from offset "+getStart()+prefix.length()+" in \n"+data);
		};
		
		switch(include) {
			case ContainedData.INCLUDE_SUFFIX:
				setStart(getStart()+prefix.length());
				setEnd(getEnd()+suffix.length());
				break;
			case ContainedData.INCLUDE_NONE:
				setStart(getStart()+prefix.length());
				break;
			case ContainedData.INCLUDE_BOTH:
				setEnd(getEnd()+suffix.length());
				break;
			case ContainedData.INCLUDE_PREFIX:
				break;
			default:	
				throw new InvalidIncludeValueException("Invalid include value: "+include);
		}
		
		init(data, start, end);
	}

	protected void init(String data, int start, int end) 
	throws FailedSubstringException {
		try {
			setSource(data.substring(getStart(), getEnd()));
		} catch(Exception e) {
			throw new FailedSubstringException("Invalid substring("+getStart()+","+getEnd()+") of : \n"+data+"\n"+e.getMessage());
		}
	}

	public String getSource() {
		return source;
	}

	protected void setSource(String source) {
		this.source = source;
	}

	public int getStart() {
		return start;
	}

	protected void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	protected void setEnd(int end) {
		this.end = end;
	}


	public String getPrefix() {
		return prefix;
	}


	public String getSuffix() {
		return suffix;
	}


	protected void setPrefix(String prefix) {
		this.prefix = prefix;
	}


	protected void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
