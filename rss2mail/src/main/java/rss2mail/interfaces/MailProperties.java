package rss2mail.interfaces;

import java.util.Properties;

import com.google.gson.JsonObject;

public interface MailProperties {
		public String getHost();
		public int getPort();
		public boolean isStarttlsEnable();
		public boolean getAuth();
		public String getUser();
		public String getPassword();
		public String getReplyTo();
		public Properties toProperties();
		public JsonObject toJson();
		public String toString();
}
