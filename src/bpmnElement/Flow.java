package bpmnElement;
/**
 * Abstract class representing the flow of choreography. 
 * @author stefa
 *
 */
public abstract class Flow {
	private String id;
	private String source;
	private String target;

	public Flow(String id, String source, String ref) {
		super();
		this.id = id;
		this.source = source;
		this.target = ref;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String ref) {
		this.target = ref;
	}

}
