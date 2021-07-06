package converter;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import bpmnElement.Field;
import bpmnElement.Message;
import bpmnElement.QoS;

/**
 * Class which iterate all the <bpmn2:message> and create an object Message with related attributes
 * Return a list of Message specified in the bpmn model (xml)
 * Notice the message without fields are not included, this because they are useless and automatically created by the modeler
 * @author Stefano Bianconi
 *
 */
public class MessageConverter extends Converter {

	public ArrayList<Message> convertAll(Document doc){
		ArrayList<Message> result = new ArrayList<Message>();

		NodeList list = doc.getElementsByTagName("bpmn2:message");

		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {

				Element element = (Element) node;

				ArrayList<Field> fields = setField(element);
				if(fields != null) {
					String id = element.getAttribute("id");
					String name = element.getAttribute("name");
					String documentation = setDoc(element);
					QoS qos = new QoS(element.getAttribute("camunda:qosreliability"), element.getAttribute("camunda:qosdurability"), element.getAttribute("camunda:qosdeadline"), element.getAttribute("camunda:qosownership"), element.getAttribute("camunda:qosliveliness"), element.getAttribute("camunda:qoslivelinessduration"), element.getAttribute("camunda:qoshistory"), element.getAttribute("camunda:qoshistorydepth"));

					Message m = new Message(id, name, documentation, qos, fields);
					result.add(m);
				}
			}
		}
		return result;
	}


	/**
	 * Read the <bpmn2:extensionElements> and look for formField to add to the ArrayList<Field> (return) containing name and type
	 * In case of enum the name is set on the form enumName [value1, value2, ..] 
	 * @param e is the bpmn2:message Element
	 * @return the field to be passed to a message being created
	 */
	private ArrayList<Field> setField(Element e) {
		ArrayList<Field> result = new ArrayList<Field>();
		NodeList list = e.getElementsByTagName("bpmn2:extensionElements");

		//There is no Field
		if(list.getLength() == 0 ) {
			return null;
		}

		else {
			Node formData = list.item(0);
			Element elementField  = (Element) formData;

			NodeList formFields = elementField.getElementsByTagName("camunda:formField");

			for (int j = 0; j < formFields.getLength(); j++) {

				Node field = formFields.item(j);
				Element fieldelement = (Element) field;
				//not enum
				if(fieldelement.getTagName()=="camunda:formField"
						&& (!fieldelement.getAttribute("type").equals("enum"))) {

					Field f = new Field(fieldelement.getAttribute("id"), fieldelement.hasAttribute("isKey"), fieldelement.getAttribute("type"));
					result.add(f);
				}
				//enum so the name is formatted
				else if (fieldelement.getTagName()=="camunda:formField"
						&& fieldelement.getAttribute("type").equals("enum")) {

					String enumName = fieldelement.getAttribute("id");
					NodeList values = fieldelement.getElementsByTagName("camunda:value");

					for (int x = 0; x < values.getLength(); x++) {
						Node camundaValue = values.item(x);
						Element camundaElement  = (Element) camundaValue;
						enumName = enumName +" "+ camundaElement.getAttribute("id");
					}

					Field f = new Field(enumName, fieldelement.hasAttribute("isKey"), fieldelement.getAttribute("type"));
					result.add(f);
				}
			}
		}
		return result;

	}
}
