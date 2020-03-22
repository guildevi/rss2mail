package rss2mail.cloudant;

import cloudant.utilities.CloudantDatabase;

public class Database extends CloudantDatabase implements rss2mail.interfaces.Database {

	public Database() throws Exception {
		super();
	}
	
	public Database(String name) throws Exception {
		super(name);
	}

}
