package bpmnElement;

/**
 * Class representig each sequence flow of the choreography task.
 * @author Stefano Bianconi
 *
 */
public class SequenceFlow extends Flow{
	private String name;
	private String doc;
	String type;

	public SequenceFlow(String id, String source, String target, String name, String doc, String type) {
		super(id, source, target);
		this.name=name;
		this.doc = doc;
		this.type=type;

	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	@Override
	public String toString() {
		return "SequenceFlow [name=" + name + ", doc=" + doc + ", getId()=" + getId() + ", getSource()=" + getSource()
		+ ", getTarget()=" + getTarget() + "type "+getType()+"]";
	}


}
