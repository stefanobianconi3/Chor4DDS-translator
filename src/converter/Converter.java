package converter;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public abstract class Converter {
	/**
	 * Set the documentation of the element being created, the <bpmn2:documentation> is considered. 
	 * @param e: xml Element 
	 * @return String: the <bpmn2:documentation> content child of the Element e. if <bpmn2:documentation> does not exist, null is returned.
	 */
	public String setDoc(Element e) {
		NodeList list = e.getElementsByTagName("bpmn2:documentation");
		if(list.getLength() != 0) {
			return list.item(0).getTextContent();
		}
		else {
			return null;
		}
	}
}
