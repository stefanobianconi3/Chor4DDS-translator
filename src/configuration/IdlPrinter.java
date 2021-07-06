package configuration;

import java.util.ArrayList;

import bpmnElement.Message;

public class IdlPrinter {
	private Directory dir;

	public IdlPrinter(Directory d) {
		this.dir = d;
	}


	/**
	 * @param messages: all the messages of the choreography with attributes. 
	 * @return idlTemplate: content of the IDL file
	 */
	public String idlTemplate(ArrayList<Message> messages) {
		String prefix = "module "+dir.getIDLC()+" {\n";
		String body = "";
		for (Message message : messages) {
			body = body +"\n"+message.getIdl();
		}
		String suffix = "\n\n };";

		return prefix +body+ suffix;
	}

	public String mpcTemplate(String n) {
		String init = "  project: dcps_java {\n\n\t idlflags      += -Wb,stub_export_include="+n+"_Export.h \\ \n\t\t\t\t\t  -Wb,stub_export_macro="+n+"_Export\n"
				+ "\t dcps_ts_flags += -Wb,export_macro="+n+"_Export\n\t idl2jniflags  += -Wb,stub_export_include="+n+"_Export.h \\ \n\t\t\t\t\t  -Wb,stub_export_macro="+n+"_Export\n"
				+ "\t dynamicflags  += "+n.toUpperCase()+"_BUILD_DLL\n\n\t specific {\n\t   jarname      = DDS_"+n+"_types\n\t }\n\n"
				+ "\t TypeSupport_Files {\n\t "+n+".idl\n\t }\n   }";
		return init;
	}
}
