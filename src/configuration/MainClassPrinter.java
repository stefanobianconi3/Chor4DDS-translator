package configuration;

import java.util.ArrayList;

import ddsApplication.Application;
import ddsApplication.Dr;
import ddsApplication.Dw;
import ddsApplication.FilteredTopic;

public class MainClassPrinter {
	private Application app;
	private Directory dir;
	private String mainClass;
	public MainClassPrinter(Application a, Directory d) {
		this.app=a;
		this.dir = d;
		this.mainClass = printMainClass(a.getName());
	}



	public String mainCode() {
		String close = "\n\t}\n}";
		return mainClass+close;

	}
	private String printMainClass(String appName) {
		String result = "package "+appName+";\n\nimport org.omg.CORBA.StringSeqHolder;\nimport OpenDDS.DCPS.*;\nimport DDS.*;\nimport "+dir.getIDLC()+".*;\n\n" ;
		result = result+"public class Main"+appName+" {\n\t public static void main(String[] args) {\n\t\t"
				+ "Setup"+appName+" setup = new Setup"+appName+"(TheParticipantFactory.WithArgs(new StringSeqHolder(args)));\n";

		result = result+writeOperation()+writeDwOp()+writeListener();
		return result;
	}

	private String writeListener() {
		String result = "";
		ArrayList<String> topics = new ArrayList<String>();
		for (Dr dr : app.getDr()) {
			if(!topics.contains(dr.getTopic().getName())) {
				topics.add(dr.getTopic().getName());
			}
		}
		if(topics.size()>0) {
			result = "\n\t\t//CREATE LISTENER FOR RECEIVE THE DATA\n";
		
		for (String string : topics) {
			result = result+"\t\t"+string+"Listener "+string.toLowerCase()+"listener = new "+string+"Listener();\n";
		}
		result = result+"\n\t\t//SET THE LISTENER TO DATA READERS\n";
		for (Dr dr : app.getDr()) {
			result = result+"\t\tsetup.getDR"+dr.getName()+"_"+dr.getQos_dr().QoSabbrDr()+"().set_listener("+dr.getTopic().getName().toLowerCase()+"listener ,DEFAULT_STATUS_MASK.value);\n";
		}
		if(app.getFilteredTopics().size()>0) {
		result = result+"\n\t\t//SET THE LISTENER TO FILTERED DATA READERS\n";
		}
		for (Dr dr : app.getDr()) {
			for (FilteredTopic ft : app.getFilteredTopics()) {
				if(ft.getDr().getName().equals(dr.getName())) {
					int hashcode = Math.abs(ft.getFilterExpression().hashCode());
					result = result+"\t\tsetup.getFiltered_"+ft.getDr().getName()+ft.getDr().getQos_dr().QoSabbrDr()+"_"+hashcode+"().set_listener("+dr.getTopic().getName().toLowerCase()+"listener ,DEFAULT_STATUS_MASK.value);\n";
				}
			}
			
		}
		}
		return result;
	}

	private String writeOperation () {
		String result = "";
		ArrayList<String> topics = new ArrayList<String>();
		for (Dw dw : app.getDw()) {
			if(!topics.contains(dw.getTopic().getName())) {
				topics.add(dw.getTopic().getName());
			}
		}
		if(topics.size()>0) {
			result = "\n\t\t//OBJECTS USED FOR WRITE OPERATION\n";
		}
		for (String string : topics) {
			result = result+"\t\t"+string+" w_"+string.toLowerCase()+" = new "+string+"();\n";
		}

		return result;
	}

	private String writeDwOp() {
		String result = "";
		for (Dw dw : app.getDw()) {
			result = result+"\t\tsetup.getNarrow_"+dw.getName()+"_"+dw.getQos_dw().QoSabbrDw()+"().write(w_"+dw.getTopic().getName().toLowerCase()+", HANDLE_NIL.value);\n";
		}
		return result;
	}

}
