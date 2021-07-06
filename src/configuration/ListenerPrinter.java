package configuration;



import bpmnElement.Field;
import ddsApplication.Application;
import ddsApplication.Topic;

public class ListenerPrinter {
	private Directory dir;
	private Application a;
	private String listenerclass;
	private String dataMethod;
	private String otherMethod;
	public ListenerPrinter(Application a, Topic t, Directory dir) {
		this.a=a;
		this.dir=dir;
		this.listenerclass = printClass(a.getName(), t.getName());
		this.dataMethod = print_data_available(t);
		this.otherMethod = printOthers();
	}

	public Application getA() {
		return a;
	}
	
	public String listenercode() {
		String close = "\n}";
		return listenerclass+dataMethod+otherMethod+close;
	}
	
	private String printClass(String appName, String topicName) {
		String result = "package "+appName+";\n\nimport DDS.*;\nimport "+dir.getIDLC()+".*;\n\n" ;
		
		return result+"public class "+topicName+"Listener extends DDS._DataReaderListenerLocalBase{\n\n";
	}
	
	private String print_data_available(Topic t) {
		String fields = "";
		
		String result="\n\t@Override\n\tpublic void on_data_available(DataReader arg0) {\n\t\t"+t.getName()+"DataReader dr = "+t.getName()+"DataReaderHelper.narrow(arg0);\n"+""
						+ "\t\tif(dr == null) {\n\t\t\tSystem.err.println(\"ERROR: on_data_available: narrow failed.\");\n\t\t\treturn;\n\t\t}\n"
						+ "\t\t"+t.getName()+"SeqHolder topicholder = new "+t.getName()+"SeqHolder(new "+t.getName()+"[] {});\n\t\t"
								+ "SampleInfoSeqHolder infoholder = new SampleInfoSeqHolder(new SampleInfo[]{});\n\n\t\t"
								+ "int  status = dr.read(topicholder, infoholder, LENGTH_UNLIMITED.value, ANY_INSTANCE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);\n\t\t"
								+ "if(status == RETCODE_OK.value) {\n\t\t\tfor (int i = 0; i < infoholder.value.length; i++) {\n\t\t\t\t"
								+ "if(infoholder.value[i].valid_data && infoholder.value[i].sample_state == NOT_READ_SAMPLE_STATE.value) {\n\t\t\t\t\t"
								+ "System.out.println(\"Received::::::\"+";
		for (Field f : t.getFields()) {
			fields = "\""+f.getName()+"= \"+topicholder.value[i]."+f.getName()+"+";
		}
		result = result+fields+"\"::::::\");\n\t\t\t\t}\n\t\t\t}\n\t\t}\n\t}";
		
		
		return result;
	}
	
	private String printOthers() {
		return "\n\n\t@Override\r\n"
				+ "	public void on_liveliness_changed(DataReader arg0, LivelinessChangedStatus arg1) {\r\n"
				+ "\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "	@Override\r\n"
				+ "	public void on_requested_deadline_missed(DataReader arg0, RequestedDeadlineMissedStatus arg1) {\r\n"
				+ "\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "	@Override\r\n"
				+ "	public void on_requested_incompatible_qos(DataReader arg0, RequestedIncompatibleQosStatus arg1) {\r\n"
				+ "\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "	@Override\r\n"
				+ "	public void on_sample_lost(DataReader arg0, SampleLostStatus arg1) {\r\n"
				+ "\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "	@Override\r\n"
				+ "	public void on_sample_rejected(DataReader arg0, SampleRejectedStatus arg1) {\r\n"
				+ "\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "	@Override\r\n"
				+ "	public void on_subscription_matched(DataReader arg0, SubscriptionMatchedStatus arg1) {\r\n"
				+ "\r\n"
				+ "	}";
	}
}
