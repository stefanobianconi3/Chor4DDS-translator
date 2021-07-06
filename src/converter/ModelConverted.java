package converter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import bpmnElement.Message;
import bpmnElement.MessageFlow;
import bpmnElement.Participant;
import bpmnElement.SequenceFlow;
import bpmnElement.Task;

/**
 * Represents the bpmn model as Java Objects.
 * @author Stefano Bianconi
 *
 */
public class ModelConverted {
	private ArrayList<Message> topics;
	private ArrayList<Participant> participants;
	private ArrayList<MessageFlow> messageFlows;
	private ArrayList<SequenceFlow> sequenceFlows;
	private ArrayList<Task> tasks;

	private FlowConverter fc;
	private MessageConverter mc;
	private ParticipantConverter pc;
	private TaskConverter tc;
	
	private String idlName;

	public ModelConverted() {
		this.topics = new ArrayList<Message>();
		this.participants = new ArrayList<Participant>();
		this.messageFlows = new ArrayList<MessageFlow>();
		this.sequenceFlows = new ArrayList<SequenceFlow>();
		this.tasks = new ArrayList<Task>();
		this.mc = new MessageConverter();
		this.pc = new ParticipantConverter();
		this.fc = new FlowConverter();
		this.tc = new TaskConverter();
		this.idlName = "";
	}

	/**
	 * Parse the bpmn model (xml) and convert all the element to java object
	 * @param path
	 */
	public void convertAll(String path) {
		System.out.println("START MODEL CONVERSION");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			// parse XML file
			Document doc = db.parse(new File(path));


			doc.getDocumentElement().normalize();
			NodeList choreography = doc.getElementsByTagName("bpmn2:choreography");
			Node choreographyNode = choreography.item(0);
			

			//Choreography element
			Element elementChoreography = (Element) choreographyNode;

			this.idlName = elementChoreography.getAttribute("camunda:idlName");
			
			//CONVERSION of messages TO JAVA OBJECT (input Document)
			this.topics=mc.convertAll(doc);

			this.participants= pc.convertAll(elementChoreography);
			this.messageFlows = fc.convertMessageFlow(elementChoreography);
			this.sequenceFlows= fc.convertSequenceFlow(elementChoreography);
			this.tasks = tc.convertAll(elementChoreography, participants, messageFlows, sequenceFlows);

		} catch (ParserConfigurationException | SAXException | IOException e) {
			System.out.println("CONVERSION PROCESS FAILED #########");
			e.printStackTrace();
		}

	}

	public String getIdlName() {
		return idlName;
	}

	/**
	 * @return all the choreography messages class representing topic
	 */
	public ArrayList<Message> getTopics() {
		return topics;
	}

	/**
	 * @return all the  choreography participants representing application
	 */
	public ArrayList<Participant> getParticipants() {
		return participants;
	}

	/**
	 * @return all the message flows
	 */
	public ArrayList<MessageFlow> getMessageFlows() {
		return messageFlows;
	}

	/**
	 * @return all the sequence flows
	 */
	public ArrayList<SequenceFlow> getSequenceFlows() {
		return sequenceFlows;
	}

	/**
	 * @return all the tasks
	 */
	public ArrayList<Task> getTasks() {
		return tasks;
	}



}
