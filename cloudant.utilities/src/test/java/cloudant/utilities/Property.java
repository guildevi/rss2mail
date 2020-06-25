package cloudant.utilities;

public class Property {
	private String name;
	private String value;
	
	public Property() {
		// TODO Auto-generated constructor stub
	}

	public Property(String name, String value) {
		setName(name);
		setValue(value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return getName()+"="+getValue();
	}
}
