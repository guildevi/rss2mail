package system.utilities;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Map;

import javax.activation.MailcapCommandMap;

import org.junit.Test;

public class PropertyManagerTest {

	@Test
	public void test() {
		
		String filenameKey = "propertymanager.properties";
		String key = "property00";
		String def = "DEFAULT";
		String value = PropertyManager.getProperty(filenameKey, key, def);
		System.out.println(String.format("%s = %s", key, value));
		//assertSame(def, value);
		
		key = "property01";
		value = PropertyManager.getProperty(filenameKey, key, def);
		System.out.println(String.format("%s = %s", key, value));
		//assertSame("PROPERTY 01", value);
		
		key = "property02";
		value = PropertyManager.getProperty(filenameKey, key, def);
		System.out.println(String.format("%s = %s", key, value));
		
		key = "ssh.auth.sock";
		value = PropertyManager.getProperty(filenameKey, key, def);
		System.out.println(String.format("%s = %s", key, value));
		
		Map<String, String> envs = System.getenv();
		Iterator<String> vars = envs.keySet().iterator();
		while(vars.hasNext()) {
			String var = vars.next();
			String val = envs.get(var);
			System.out.println(String.format("%s = %s", var, val));
		}
		
	}

}
