package rss2mail.ibmcloud.pup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import rss2mail.exceptions.FailedSubstringException;
import rss2mail.exceptions.InvalidIncludeValueException;
import rss2mail.exceptions.InvalidSourceDateFormatException;
import rss2mail.exceptions.PrefixNotFoundException;
import rss2mail.exceptions.SuffixNotFoundException;

public class StatusDate extends ContainedDate {

	public static SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
	public static String PREFIX = Integer.toString(new GregorianCalendar().get(Calendar.YEAR))+"-";
	public static String SUFFIX = " UTC";


	public StatusDate(String data, String prefix, String suffix,int offset) 
	throws PrefixNotFoundException, SuffixNotFoundException, InvalidIncludeValueException, FailedSubstringException,InvalidSourceDateFormatException {
		super(data, prefix, suffix,offset,ContainedDate.INCLUDE_BOTH,DATEFORMAT);
	}

	public StatusDate(String data,int offset) 
	throws PrefixNotFoundException, SuffixNotFoundException, InvalidIncludeValueException, FailedSubstringException,InvalidSourceDateFormatException {
		super(data, PREFIX, SUFFIX,offset,ContainedDate.INCLUDE_BOTH,DATEFORMAT);
	}
}
