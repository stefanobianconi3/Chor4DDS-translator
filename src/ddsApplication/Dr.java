package ddsApplication;


import bpmnElement.Message;
import bpmnElement.QosTask;
/**
 * Representation of Data Reader
 * @author Stefano Bianconi
 *
 */
public class Dr {
	private Sub sub;
	private QosTask qos_dr;
	private String name;
	private Message topic;

	public Dr(Sub sub, QosTask qos_dr, String name, Message topic) {
		super();
		this.name=name;
		this.sub = sub;
		this.qos_dr = qos_dr;
		this.topic=topic;

	}



	public String getName() {

		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Sub getSub() {
		return sub;
	}
	public void setSub(Sub sub) {
		this.sub = sub;
	}
	public QosTask getQos_dr() {
		return qos_dr;
	}
	public void setQos_dr(QosTask qos_dr) {
		this.qos_dr = qos_dr;
	}

	public Message getTopic() {
		return topic;
	}

	public void setTopic(Message topic) {
		this.topic = topic;
	}

}
