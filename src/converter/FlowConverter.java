package converter;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import bpmnElement.MessageFlow;
import bpmnElement.SequenceFlow;

/**
 * Class responsible to convert the message flow and sequence flow of the bpmn file.
 * @author Stefano Bianconi
 *
 */
public class FlowConverter extends Converter{


	public ArrayList<MessageFlow> convertMessageFlow(Element e){
		ArrayList<MessageFlow> result = new ArrayList<MessageFlow>();
		NodeList mflows = e.getElementsByTagName("bpmn2:messageFlow");

		for (int i = 0; i < mflows.getLength(); i++) {
			Node mflowNode = mflows.item(i);
			Element mflowElement = (Element) mflowNode;

			String id = mflowElement.getAttribute("id");
			String source = mflowElement.getAttribute("sourceRef"); 
			String target = mflowElement.getAttribute("targetRef");
			String filter = setFilter(mflowElement);
			String messageRef = mflowElement.getAttribute("messageRef");
			MessageFlow mf = new MessageFlow(id, source, target, filter, messageRef);
			result.add(mf);
		}
		return result;


	}

	public ArrayList<SequenceFlow> convertSequenceFlow(Element e){
		ArrayList<SequenceFlow> result = new ArrayList<SequenceFlow>();
		NodeList sflows = e.getElementsByTagName("bpmn2:sequenceFlow");

		for (int i = 0; i < sflows.getLength(); i++) {
			Node sflowNode = sflows.item(i);
			Element sflowElement = (Element) sflowNode;

			String id = sflowElement.getAttribute("id");
			String source = sflowElement.getAttribute("sourceRef"); //remove spaces if any
			String target = sflowElement.getAttribute("targetRef");
			String name = (sflowElement.hasAttribute("name")) ? sflowElement.getAttribute("name") :  null;
			String doc = setDoc(sflowElement);
			SequenceFlow sf = new SequenceFlow(id, source, target, name, doc, null);
			result.add(sf);
		}
		return result;


	}

	private String setFilter(Element e) {
		if(e.hasAttribute("FilterInitiating")) {
			return e.getAttribute("FilterInitiating");
		}
		else if (e.hasAttribute("FilterReceiver")) {
			return e.getAttribute("FilterReceiver");
		}
		else return "";
	}
}
