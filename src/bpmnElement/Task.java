package bpmnElement;

import java.util.ArrayList;

/**
 * Class representing Choreography tasks from the model. 
 * @author Stefano Bianconi
 *
 */
public class Task extends Element{


	private String dp;
	private String initiatingParticipantRef;
	private QosTask qos;
	private ArrayList<SequenceFlow> sequenceFlow;
	private ArrayList<Participant> participants;
	private ArrayList<MessageFlow> messageFlow;

	public Task(String id, String name, String documentation, String dp, String initiatingParticipantRef, QosTask qos,
			ArrayList<SequenceFlow> sequenceFlow, ArrayList<Participant> participants, ArrayList<MessageFlow> messageFlow) {
		super(id, name, documentation);
		this.dp=dp;
		this.initiatingParticipantRef = initiatingParticipantRef;
		this.qos=qos;
		this.sequenceFlow = sequenceFlow;
		this.participants=participants;
		this.messageFlow= messageFlow;
	}

	public ArrayList<SequenceFlow> getSequenceFlow() {
		return sequenceFlow;
	}

	public void setSequenceFlow(ArrayList<SequenceFlow> sequenceFlow) {
		this.sequenceFlow = sequenceFlow;
	}

	public String getDp() {
		return dp;
	}

	public void setDp(String dp) {
		this.dp = dp;
	}

	public String getInitiatingParticipantRef() {
		return initiatingParticipantRef;
	}

	public void setInitiatingParticipantRef(String initiatingParticipantRef) {
		this.initiatingParticipantRef = initiatingParticipantRef;
	}

	public QosTask getQos() {
		return qos;
	}

	public void setQos(QosTask qos) {
		this.qos = qos;
	}

	public ArrayList<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(ArrayList<Participant> participants) {
		this.participants = participants;
	}

	public ArrayList<MessageFlow> getMessageFlow() {
		return messageFlow;
	}

	public void setMessageFlow(ArrayList<MessageFlow> messageFlow) {
		this.messageFlow = messageFlow;
	}

	/**
	 * Get the id of the non initiating participant. Works with a task composed of two participant.
	 * @return String id: reference of the receiver of the message
	 */
	public String getReceiver() {
		String part = null;
		for (Participant p : this.participants) {
			if(!(p.getId().equals(initiatingParticipantRef))) {
				part = p.getId();
			}
		}
		return part;
	}
	
	public ArrayList<String> getSources() {
		ArrayList<String> result = new ArrayList<String>();
		for (MessageFlow f : this.messageFlow) {
				result.add(f.getSource());
			
		}
		return result;
	}
	
	public ArrayList<String> getTargets() {
		ArrayList<String> result = new ArrayList<String>();
		for (MessageFlow f : this.messageFlow) {
				result.add(f.getTarget());
			
		}
		return result;
	}

	@Override
	public String toString() {
		return "Task [dp=" + dp + ", initiatingParticipantRef=" + initiatingParticipantRef + ", qos=" + qos
				+  "participants=" + participants + ", messageFlow=" + messageFlow
				+ "]";
	}
}
