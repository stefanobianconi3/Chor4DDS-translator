package bpmnElement;

/**
 * This class represents the single attribute of each Topic (Choreography message).
 * The fields of type enum are modified on the class [translator]/MessageConverter in order to have the name a its values in the variable name below
 * @author Stefano Bianconi
 *
 */
public class Field {

	private String name;
	private boolean key;
	private String type;


	public Field(String name, boolean key, String type) {
		super();
		this.name = name;
		this.key = key;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isKey() {
		return key;
	}
	public void setKey(boolean key) {
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Field [name=" + name + ", key=" + key + ", type=" + type + "]";
	}
}
