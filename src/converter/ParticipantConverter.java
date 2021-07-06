package converter;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import bpmnElement.Participant;

/**
 * Convert each choreography participant to a java object Participant.
 * @author Stefano Bianconi
 *
 */
public class ParticipantConverter extends Converter{

	/**
	 * Convert all the choreography participant and set the ArrayList<Participant>
	 * @param e: The  <bpmn2:choreography> element
	 * @return ArrayList where each participant has id, name and documentation if provided in the xml file
	 */
	public ArrayList<Participant> convertAll(Element e){
		ArrayList<Participant> result = new ArrayList<Participant>();

		NodeList participants = e.getElementsByTagName("bpmn2:participant");

		for (int i = 0; i < participants.getLength(); i++) {
			Node participantNode = participants.item(i);
			Element participantElement = (Element) participantNode;

			String id = participantElement.getAttribute("id");
			String name = participantElement.getAttribute("name").replaceAll(" ", ""); //remove spaces if any
			String documentation = setDoc(participantElement);
			Participant p = new Participant(id, name, documentation);
			result.add(p);
		}

		return result;
	}


}
