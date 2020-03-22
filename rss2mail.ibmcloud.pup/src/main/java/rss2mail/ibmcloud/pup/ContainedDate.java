package rss2mail.ibmcloud.pup;

import java.text.SimpleDateFormat;
import java.util.Date;

import rss2mail.exceptions.FailedSubstringException;
import rss2mail.exceptions.InvalidIncludeValueException;
import rss2mail.exceptions.InvalidSourceDateFormatException;
import rss2mail.exceptions.PrefixNotFoundException;
import rss2mail.exceptions.SuffixNotFoundException;

public class ContainedDate extends ContainedData {
	
	protected  SimpleDateFormat sourceDateFormat = null;
	private Date date ;
	
	public ContainedDate() {
		super();
	}

	public ContainedDate(String data, String prefix,String suffix,int include,SimpleDateFormat sourceDateFormat) 
	throws PrefixNotFoundException, SuffixNotFoundException, InvalidIncludeValueException, FailedSubstringException,InvalidSourceDateFormatException {
		init(data,prefix,suffix,0,include,sourceDateFormat);
	}
	
	public ContainedDate(String data, String prefix,String suffix,int offset,int include,SimpleDateFormat sourceDateFormat) 
	throws PrefixNotFoundException, SuffixNotFoundException, InvalidIncludeValueException, FailedSubstringException,InvalidSourceDateFormatException {
		init(data,prefix,suffix,offset,include,sourceDateFormat);
	}
		
	public void init(String data, String prefix, String suffix,int offset,int include,SimpleDateFormat sourceDateFormat) 
	throws PrefixNotFoundException, SuffixNotFoundException, InvalidIncludeValueException, FailedSubstringException,InvalidSourceDateFormatException {

		super.init(data, prefix, suffix, offset, include);
		setSourceDateFormat(sourceDateFormat);
		try {
			setDate(getSourceDateFormat().parse(getSource()));
		} catch(Exception e) {
			throw new InvalidSourceDateFormatException("Invalid date format "+getSource()+" : "+e.getMessage()
					+"\nPrefix "+prefix+", suffix "+suffix
					+"\nContent ["+data+"]");
		}
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String changeDateFormat(String data, SimpleDateFormat targetDateFormat) {
		try {
			if(getDate()!=null) {
				String targetDate = targetDateFormat.format(getDate());
				data = data.replaceAll(getSource(), targetDate);
			} else logger.warning("Date is null");
		} catch(Exception e) {
			logger.warning("Failed to serialize "+getDate()+e.toString());
		}
		return data;
	}

	public SimpleDateFormat getSourceDateFormat() {
		return sourceDateFormat;
	}

	public void setSourceDateFormat(SimpleDateFormat sourceDateFormat) {
		this.sourceDateFormat = sourceDateFormat;
	}
	
}
