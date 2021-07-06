package ddsApplication;

import java.util.ArrayList;

import bpmnElement.Field;
import bpmnElement.Message;
import bpmnElement.QoS;

public class Topic extends Message{
	private String dp;
	public Topic(String id, String name, String documentation, QoS qos, ArrayList<Field> fields, String dp) {
		super(id, name, documentation, qos, fields);
		this.dp=dp;
	}
	public String getDp() {
		return dp;
	}
	public void setDp(String dp) {
		this.dp = dp;
	}

}
