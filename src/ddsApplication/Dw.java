package ddsApplication;

import bpmnElement.Message;
import bpmnElement.QosTask;
/**
 * Representation of Data Writer
 * @author Stefano Bianconi
 *
 */
public class Dw {
	private Pub pub;
	private QosTask qos_dw;
	private String name;
	private Message topic;


	public Dw(Pub pub, QosTask qos_dw, String name, Message topic) {
		super();
		this.name=name;
		this.pub = pub;
		this.qos_dw = qos_dw;
		this.setTopic(topic);
	}

	public String getName() {

		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Pub getPub() {
		return pub;
	}
	public void setPub(Pub pub) {
		this.pub = pub;
	}
	public QosTask getQos_dw() {
		return qos_dw;
	}
	public void setQos_dw(QosTask qos_dw) {
		this.qos_dw = qos_dw;
	}

	public Message getTopic() {
		return topic;
	}

	public void setTopic(Message topic) {
		this.topic = topic;
	}

}
