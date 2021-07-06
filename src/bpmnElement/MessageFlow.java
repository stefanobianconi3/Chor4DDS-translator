package bpmnElement;


/**
 * Class representing Message Flow. Used for the conversion from xml and to get the filter expression on code generation
 * @author Stefano Bianconi
 *
 */
public class MessageFlow extends Flow{
	private String filterExpression;
	private String messageRef;

	public MessageFlow(String id, String source, String target, String filterExpression, String messageRef) {
		super(id, source, target);
		this.filterExpression = filterExpression;
		this.messageRef = messageRef;
	}

	public String getFilterExpression() {
		return filterExpression;
	}

	public void setFilterExpression(String filterExpression) {
		this.filterExpression = filterExpression;
	}

	public String getMessageRef() {
		return messageRef;
	}

	public void setMessageRef(String messageRef) {
		this.messageRef = messageRef;
	}

	@Override
	public String toString() {
		return "MessageFlow [id=" + this.getId() + "source "+this.getSource()+" target "+this.getTarget()+" filterexpr: "+this.filterExpression+" ]";
	}

}
