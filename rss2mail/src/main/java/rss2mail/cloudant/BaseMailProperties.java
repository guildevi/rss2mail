package rss2mail.cloudant;

import java.util.Properties;

import com.google.gson.JsonObject;

import cloudant.utilities.CloudantObject;

public class BaseMailProperties extends CloudantObject implements rss2mail.interfaces.MailProperties {
	
	public final static String PROP_HOST="mail.smtp.host";
	public final static String PROP_PORT="mail.smtp.port";
	public final static String PROP_USER="mail.smtp.user";
	public final static String PROP_REPLYTO="mail.msg.replyTo";
	public final static String PROP_PASSWORD="mail.smtp.password";
	public final static String PROP_AUTH="mail.smtp.auth";
	public final static String PROP_STARTTLS_ENABLE="mail.smtp.starttls.enable";

	private String host = null;
	private int port = 0;
	private boolean starttlsEnable = true;
	private boolean auth = true;
	private String user = null;
	private String password = null;
	private String replyTo = null;
	
	public BaseMailProperties(String _id) {
		super(_id);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isStarttlsEnable() {
		return starttlsEnable;
	}

	public void setStarttlsEnable(boolean starttlsEnable) {
		this.starttlsEnable = starttlsEnable;
	}

	public boolean getAuth() {
		return auth;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public Properties toPropeties() {
		Properties properties = new Properties();
		properties.setProperty(PROP_HOST, getHost());
		properties.setProperty(PROP_PORT, Integer.toString(getPort()));
		properties.setProperty(PROP_USER, getUser());
		properties.setProperty(PROP_PASSWORD, getPassword());
		properties.setProperty(PROP_AUTH, Boolean.toString(getAuth()));
		properties.setProperty(PROP_STARTTLS_ENABLE, Boolean.toString(isStarttlsEnable()));
		return properties;
	}
	
	public JsonObject toJson() {
		JsonObject jsonObject = super.toJson();
		jsonObject.addProperty(PROP_HOST, getHost());
		jsonObject.addProperty(PROP_PORT, getPort());
		jsonObject.addProperty(PROP_USER, getUser());
		jsonObject.addProperty(PROP_REPLYTO, getReplyTo());
		if(getPassword()==null) jsonObject.addProperty(PROP_PASSWORD, "UNSET");
		else jsonObject.addProperty(PROP_PASSWORD, "SET");
		jsonObject.addProperty(PROP_PORT, getPort());
		return jsonObject;
	}
}
