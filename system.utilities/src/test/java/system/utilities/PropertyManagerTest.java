package system.utilities;

import org.junit.Test;

public class PropertyManagerTest {

	@Test
	public void test() {
		
		String filenameKey = "propertymanager.properties";
		String key = "unset.property";
		String def = "DEFAULT PROPERTY VALUE";
		String value = PropertyManager.getProperty(filenameKey, key, def);
		System.out.println(String.format("%s = %s", key, value));
		//assertSame(def, value);
		
		key = "java.property";
		value = PropertyManager.getProperty(filenameKey, key, def);
		System.out.println(String.format("%s = %s", key, value));
		//assertSame("PROPERTY 01", value);
		
		key = "java.properties.file.property";
		value = PropertyManager.getProperty(filenameKey, key, def);
		System.out.println(String.format("%s = %s", key, value));
		
		key = "environment.property";
		value = PropertyManager.getProperty(filenameKey, key, def);
		System.out.println(String.format("%s = %s", key, value));
		
		key = "environment.properties.file.property";
		value = PropertyManager.getProperty(filenameKey, key, def);
		System.out.println(String.format("%s = %s", key, value));
		
		/*Map<String, String> envs = System.getenv();
		Iterator<String> vars = envs.keySet().iterator();
		while(vars.hasNext()) {
			String var = vars.next();
			String val = envs.get(var);
			System.out.println(String.format("%s = %s", var, val));
		}*/
		
	}

}
