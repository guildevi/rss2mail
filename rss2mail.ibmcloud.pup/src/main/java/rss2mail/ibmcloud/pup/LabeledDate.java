package rss2mail.ibmcloud.pup;

import java.text.SimpleDateFormat;

import rss2mail.exceptions.FailedSubstringException;
import rss2mail.exceptions.InvalidIncludeValueException;
import rss2mail.exceptions.InvalidSourceDateFormatException;
import rss2mail.exceptions.PrefixNotFoundException;
import rss2mail.exceptions.SuffixNotFoundException;

public class LabeledDate extends ContainedDate {

	public static SimpleDateFormat DATEFORMAT=new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z");
	private String label ;
	public static String SUFFIX  = " GMT";

	
	public LabeledDate(String data, String label, String suffix) 
	throws PrefixNotFoundException, SuffixNotFoundException, InvalidIncludeValueException, FailedSubstringException,InvalidSourceDateFormatException {
		super(data,LabeledDate.getLabelPrefix(label),suffix,0,ContainedDate.INCLUDE_SUFFIX,DATEFORMAT);
		setLabel(label);
	}
	
	public LabeledDate(String data, String label) 
	throws PrefixNotFoundException, SuffixNotFoundException, InvalidIncludeValueException, FailedSubstringException,InvalidSourceDateFormatException {
		super(data,LabeledDate.getLabelPrefix(label),SUFFIX,0,ContainedDate.INCLUDE_SUFFIX,DATEFORMAT);
		setLabel(label);
	}
	
	public LabeledDate(String data, String label, int offset) 
	throws PrefixNotFoundException, SuffixNotFoundException, InvalidIncludeValueException, FailedSubstringException,InvalidSourceDateFormatException {
		super(data,LabeledDate.getLabelPrefix(label),SUFFIX,offset,ContainedDate.INCLUDE_SUFFIX,DATEFORMAT);
		setLabel(label);
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public static String getLabelPrefix(String label) {
		return label+": ";
	}
	
	public String format(String content,SimpleDateFormat targetDateFormat) {
		return content.replaceAll(getSource(), targetDateFormat.format(getDate()));
	}
}
