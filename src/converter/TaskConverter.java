package converter;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import bpmnElement.MessageFlow;
import bpmnElement.Participant;
import bpmnElement.QosTask;
import bpmnElement.SequenceFlow;
import bpmnElement.Task;
/**
 * Class reponsible to convert all the choreography task from the bpmn model to Task java object.
 * @author Stefano Bianconi
 *
 */
public class TaskConverter extends Converter{

	public ArrayList<Task> convertAll(Element e, ArrayList<Participant> participants, ArrayList<MessageFlow> messageFlows, 
			ArrayList<SequenceFlow> sequenceFlows) {

		ArrayList<Task> result = new ArrayList<Task>();

		NodeList tflows = e.getElementsByTagName("bpmn2:choreographyTask");

		for (int i = 0; i < tflows.getLength(); i++) {
			Node tflowNode = tflows.item(i);
			Element tflowElement = (Element) tflowNode;

			String id = tflowElement.getAttribute("id");
			String name = tflowElement.getAttribute("name");
			String doc = setDoc(tflowElement);
			String dp = tflowElement.getAttribute("camunda:dp");
			String initiatingParticipant = tflowElement.getAttribute("initiatingParticipantRef");
			QosTask qos = setQos(tflowElement);
			ArrayList<SequenceFlow> seqFlows = setSequenceFlow(tflowElement, sequenceFlows);
			ArrayList<Participant> pps = setParticipants(tflowElement, participants);
			ArrayList<MessageFlow> ms = setMessageFlows(tflowElement, messageFlows);


			Task t = new Task(id, name, doc, dp, initiatingParticipant, qos, seqFlows, pps, ms);

			result.add(t);
		}
		return result;

	}

	private QosTask setQos(Element e) {
		String rel = e.getAttribute("camunda:qosreliability");
		String dur = e.getAttribute("camunda:qosdurability");
		String dead = e.getAttribute("camunda:qosdeadline");
		String own = e.getAttribute("camunda:qosownership");
		String liv = e.getAttribute("camunda:qosliveliness");
		String livdur = e.getAttribute("camunda:qoslivelinessduration");
		String his = e.getAttribute("camunda:qoshistory");
		String hisdpt = e.getAttribute("camunda:qoshistorydepth");
		String ownstr = e.getAttribute("camunda:qosownershipstrength");
		String timefil = e.getAttribute("camunda:qostimebasedfilter");
		QosTask qos = new QosTask(rel, dur, dead, own, liv, livdur, his, hisdpt, ownstr, timefil);
		return qos;
	}

	private ArrayList<Participant> setParticipants(Element e, ArrayList<Participant> participants){
		ArrayList<Participant> result = new ArrayList<Participant>();

		NodeList partcipantList = e.getElementsByTagName("bpmn2:participantRef");

		for (int i = 0; i < partcipantList.getLength(); i++) {
			Node partNode = partcipantList.item(i);
			Element pElement = (Element) partNode;

			for (Participant p : participants) {
				if(pElement.getTextContent().equals(p.getId())) {
					result.add(p);
				}
			}

		}

		return result;
	}

	private ArrayList<MessageFlow> setMessageFlows(Element e, ArrayList<MessageFlow> messages){
		ArrayList<MessageFlow> result = new ArrayList<MessageFlow>();

		NodeList messageList = e.getElementsByTagName("bpmn2:messageFlowRef");

		for (int i = 0; i < messageList.getLength(); i++) {
			Node mNode = messageList.item(i);
			Element mElement = (Element) mNode;

			for (MessageFlow m : messages) {
				if(mElement.getTextContent().equals(m.getId())) {
					result.add(m);
				}

			}

		}

		return result;
	}

	private ArrayList<SequenceFlow> setSequenceFlow(Element e, ArrayList<SequenceFlow> sflows) {
		ArrayList<SequenceFlow> result = new ArrayList<SequenceFlow>();

		NodeList incoming = e.getElementsByTagName("bpmn2:incoming");
		for (int i = 0; i < incoming.getLength(); i++) {
			Node iNode = incoming.item(i);
			Element iElement = (Element) iNode;
			for (SequenceFlow s : sflows) {
				if(iElement.getTextContent().equals(s.getId())) {
					s.setType("incoming");
					result.add(s);
				}
			}

		}

		NodeList outgoing = e.getElementsByTagName("bpmn2:outgoing");
		for (int i = 0; i < outgoing.getLength(); i++) {
			Node oNode = outgoing.item(i);
			Element oElement = (Element) oNode;
			for (SequenceFlow s : sflows) {
				if(oElement.getTextContent().equals(s.getId())) {
					s.setType("outgoing");
					result.add(s);
				}
			}

		}

		return result;
	}


}
