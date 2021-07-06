package bpmnElement;

/**
 * Abstract class for the representation of bpmn element.
 * @author Stefano Bianconi
 *
 */
public abstract class Element {
	private String id;
	private String name;
	private String documentation;


	public Element(String id, String name, String documentation) {
		this.id = id;
		this.name = name;
		this.documentation = documentation;
	}

	public String getDocumentation() {
		return documentation;
	}


	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
