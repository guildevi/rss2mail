package rss2mail.ibmcloud.pup;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;
import java.net.URLClassLoader;

import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import rss2mail.RssToMail;

class TestRssToMail {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//-Drss2mail.properties=PupRssToMail 
		//-Drss2mail.saxhandler.class=rss2mail.ibmcloud.pup.PupSaxHandler 
		//-Drss2mail.sender.class=rss2mail.ibmcloud.pup.PupMailSender
	}
	
	@Test
	void test() {
		System.setProperty(RssToMail.ARG_PROPERTIES, "PupRssToMail");
		System.setProperty(RssToMail.ARG_SAXHANDLER_CLASS, "rss2mail.ibmcloud.pup.PupSaxHandler");
		System.setProperty(RssToMail.ARG_SENDER_CLASS, "rss2mail.ibmcloud.pup.PupMailSendere");

		RssToMail.run();
	}

}
