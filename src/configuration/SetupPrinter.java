package configuration;

import java.util.ArrayList;
import java.util.HashMap;
import ddsApplication.Application;
import ddsApplication.Dr;
import ddsApplication.Dw;
import ddsApplication.FilteredTopic;
import ddsApplication.Pub;
import ddsApplication.Sub;
import ddsApplication.Topic;

public class SetupPrinter {
	private Application app;
	private Directory dir;
	private String classInit;
	private String dps;
	private String construct;
	private String getters;
	private String topQos;
	private String dwQos;
	private String drQos;



	public SetupPrinter(Application a, Directory d) {
		this.app = a;
		this.dir = d;
		setupCode();
	}

	public String getSetupCode(){
		return classInit+dps+construct+getters+topQos+dwQos+drQos+"\n}";
	}

	private void setupCode() {
		this.classInit = printSetupClass(app.getName());
		this.dps = printAttrs(app.getDps(), app.getPublisher(), app.getSubscriber(), app.getTopic(), app.getDw(), app.getDr(), app.getFilteredTopics());
		this.construct = printConstructor(app);
		this.getters = printGetters(app.getDps(), app.getPublisher(), app.getSubscriber(), app.getTopic(), app.getDw(), app.getDr(), app.getFilteredTopics());
		this.topQos = printTopicQos(app);
		this.dwQos = printDwQoS(app);
		this.drQos = printDrQos(app);

	}

	private String printSetupClass(String appName) {
		String result = "package "+appName+";\n\nimport DDS.*;\nimport OpenDDS.DCPS.*;\nimport "+dir.getIDLC()+".*;\n\n" ;
		result = result + "public class Setup"+appName+" {\n";
		return result;
	}

	private String printAttrs(ArrayList<String> dps, ArrayList<Pub> pubs, ArrayList<Sub> subs, 
			ArrayList<Topic> topics, ArrayList<Dw> dw, ArrayList<Dr>dr, ArrayList<FilteredTopic> filtertopics) {
		String attrs = "";
		for (String dp : dps) {
			attrs = attrs+"\tprivate DomainParticipant dp_"+dp+";\n";
		}
		for (Pub pub : pubs) {
			attrs = attrs+"\tprivate Publisher "+pub.getName()+";\n";
		}
		for (Sub sub : subs) {
			attrs = attrs+"\tprivate Subscriber "+sub.getName()+";\n";
		}
		for (Topic top : topics) {

			attrs = attrs+"\tprivate Topic "+top.getName()+"_"+top.getDp()+";\n";
		}
		if(filtertopics.size() > 0) {
			for (FilteredTopic ftopic : filtertopics) {

				int hashcode = Math.abs(ftopic.getFilterExpression().hashCode());
				attrs = attrs+"\tprivate String [] placeholder_"+ftopic.getDr().getTopic().getName()+"_"+hashcode+" = {";
				for (String i : ftopic.getPlaceholder()) {
					attrs = attrs +"\""+i+"\"" + ",";
				}
				attrs = attrs.substring(0, attrs.length() - 1) +"};\n";

			}
		}
		if(filtertopics.size() > 0) {
			for (FilteredTopic ftopic : filtertopics) {

				int hashcode = Math.abs(ftopic.getFilterExpression().hashCode());
				attrs = attrs+"\tprivate ContentFilteredTopic filtered_"+ftopic.getDr().getTopic().getName()+"_"+hashcode+";\n";
			}
		}
		for (Dw d : dw) {

			attrs = attrs+"\tprivate DataWriter "+d.getName()+"_"+d.getQos_dw().QoSabbrDw()+";\n";
			attrs = attrs+"\tprivate "+d.getTopic().getName()+"DataWriter "+"narrow_"+d.getName()+"_"+d.getQos_dw().QoSabbrDw()+";\n";
		}
		for (Dr d : dr) {
			attrs = attrs+"\tprivate DataReader "+d.getName()+"_"+d.getQos_dr().QoSabbrDr()+";\n";
		}
		if(filtertopics.size() > 0) {
			for (FilteredTopic ftopic : filtertopics) {

				int hashcode = Math.abs(ftopic.getFilterExpression().hashCode());
				attrs = attrs+"\tprivate DataReader filtered_"+ftopic.getDr().getName()+ftopic.getDr().getQos_dr().QoSabbrDr()+"_"+hashcode+";\n";
			}
		}
		attrs = attrs+"\n\n";
		return attrs;
	}

	private String printConstructor(Application a) {

		String construct = "\tpublic Setup"+a.getName()+"(DomainParticipantFactory dpf) {\n\n";
		construct = construct+"\t\t//DOMAIN PARTICIPANTS CREATION\n";
		for (String dp : a.getDps()) {
			construct = construct+"\t\tthis.dp_"+dp+" = dpf.create_participant("+dp+", PARTICIPANT_QOS_DEFAULT.get(), null, DEFAULT_STATUS_MASK.value);\n"
					+ "\t\tif (dp_"+dp+" == null) {\n\t\t\tSystem.out.println (\"Domain Participant creation failed\");\n\t\t\treturn;\n\t\t}\n";

		}
		construct = construct+"\n";
		construct = construct+"\t\t//PUBLISHER AND SUBSCRIBER CREATION\n";
		for(Pub p : a.getPublisher()) {
			construct = construct+"\t\tthis."+p.getName()+" = "+"dp_"+p.getDp()+".create_publisher(PUBLISHER_QOS_DEFAULT.get(), null, DEFAULT_STATUS_MASK.value);\n";

		}
		construct = construct+"\n";
		for(Sub s : a.getSubscriber()) {
			construct = construct+"\t\tthis."+s.getName()+" = "+"dp_"+s.getDp()+".create_subscriber(SUBSCRIBER_QOS_DEFAULT.get(), null, DEFAULT_STATUS_MASK.value);\n";

		}
		construct = construct+"\n";
		construct = construct+"\t\t//TOPIC REGISTRATION ON DOMAIN PARTICIPANT\n";
		ArrayList<String> topic = new ArrayList<String>();
		for (Topic t : a.getTopic()) {
			if(!(topic.contains(t.getName()))) {
				topic.add(t.getName());
			}
		}
		for (String tn : topic) {
			construct = construct +"\t\t"+tn+"TypeSupportImpl "+"servant"+tn+" = new "+tn+"TypeSupportImpl();\n";
		}
		construct = construct+"\n";
		for(String t : topic) {
			for (String dp : a.getDps()) {
				construct = construct+"\t\tif(servant"+t+".register_type(dp_"+dp+", \"\") != RETCODE_OK.value) {\n\t\t\tSystem.out.println (\"register_type failed\");\n\t\t\treturn;\n\t\t}\n";
			}
		}
		construct = construct+"\n";
		construct = construct+"\t\t//TOPIC CREATION\n";
		for(Topic t: a.getTopic()) {
			if(t.getQos().isDefaultQoS()) {
				construct = construct+"\t\tthis."+t.getName()+"_"+t.getDp()+" = dp_"+t.getDp()+".create_topic(\""+t.getName()+"Topic\", servant"+t.getName()+".get_type_name(), TOPIC_QOS_DEFAULT.get(),"
						+ " null, DEFAULT_STATUS_MASK.value);\n";
			}
			else {
				construct = construct+"\t\tthis."+t.getName()+"_"+t.getDp()+" = dp_"+t.getDp()+".create_topic(\""+t.getName()+"Topic\", servant"+t.getName()+".get_type_name(), get"+t.getName()+"QoS(dp_"+t.getDp()+"),"
						+ " null, DEFAULT_STATUS_MASK.value);\n";
			}
		}
		if(a.getFilteredTopics().size()>0) {
			construct = construct+"\n\t\t//TOPIC FILTERED CREATION\n";
			for (FilteredTopic tf : a.getFilteredTopics()) {
				int hashcode = Math.abs(tf.getFilterExpression().hashCode());				
				construct = construct+ "\t\tthis.filtered_"+tf.getDr().getTopic().getName()+"_"+hashcode+" = dp_"+tf.getDr().getSub().getDp()+".create_contentfilteredtopic(\""+
						tf.getDr().getTopic().getName()+"Filtered\", "+ tf.getDr().getTopic().getName()+"_"+tf.getDr().getSub().getDp()+", \""+tf.getFilterModified()+"\", placeholder_"+tf.getDr().getTopic().getName()+"_"+hashcode+");\n";

			}
		}
		if(a.getFilteredTopics().size()>0) {
			construct = construct+"\n\t\t//DATA READER FILTERED CREATION\n";
			for (FilteredTopic tf : a.getFilteredTopics()) {
				int hashcode = Math.abs(tf.getFilterExpression().hashCode());
				if(tf.getDr().getQos_dr().isDefaultQoS()) {
					construct = construct+ "\t\tthis.filtered_"+tf.getDr().getName()+tf.getDr().getQos_dr().QoSabbrDr()+"_"+hashcode+" = sub_"+tf.getDr().getSub().getDp()+".create_datareader(filtered_"+tf.getDr().getTopic().getName()+"_"+hashcode+
							", DATAREADER_QOS_DEFAULT.get(), null, DEFAULT_STATUS_MASK.value);\n";
				}
				else {
					construct = construct+ "\t\tthis.filtered_"+tf.getDr().getName()+tf.getDr().getQos_dr().QoSabbrDr()+"_"+hashcode+" = sub_"+tf.getDr().getSub().getDp()+".create_datareader(filtered_"+tf.getDr().getTopic().getName()+"_"+hashcode+""
							+ ", get"+tf.getDr().getName()+"_"+tf.getDr().getQos_dr().QoSabbrDr()+"QoS(sub_"+tf.getDr().getSub().getDp()+"), null, DEFAULT_STATUS_MASK.value);\n";
				}
				construct = construct + "\t\tif(filtered_"+tf.getDr().getName()+tf.getDr().getQos_dr().QoSabbrDr()+"_"+hashcode+" == null) {\n\t\t\tSystem.out.println(\"Error DataReader creation\");\n\t\t\treturn;\n\t\t}\n";
			}
		}

		construct = construct+"\n";
		construct = construct+"\t\t//DATAREADER AND DATAWRITER CREATION\n";
		for(Dw dws: a.getDw()) {
			if(dws.getQos_dw().isDefaultQoSdw()) {
				construct = construct+"\n\t\tthis."+dws.getName()+"_"+dws.getQos_dw().QoSabbrDw()+" = pub_"+dws.getPub().getDp()+".create_datawriter("+dws.getTopic().getName()+"_"+dws.getPub().getDp()+" "
						+ ", DATAWRITER_QOS_DEFAULT.get(), null, DEFAULT_STATUS_MASK.value);\n";
			}
			else {
				construct = construct+"\n\t\tthis."+dws.getName()+"_"+dws.getQos_dw().QoSabbrDw()+" = pub_"+dws.getPub().getDp()+".create_datawriter("+dws.getTopic().getName()+"_"+dws.getPub().getDp()+" "
						+ ", "+ "get"+dws.getName()+"_"+dws.getQos_dw().QoSabbrDw()+"QoS(pub_"+dws.getPub().getDp()+"), null, DEFAULT_STATUS_MASK.value);\n";
			}



			construct = construct + "\t\tif("+dws.getName()+"_"+dws.getQos_dw().QoSabbrDw()+" == null) {\n\t\t\tSystem.out.println(\"Error DataWriter creation\");\n\t\t\treturn;\n\t\t}\n";
			construct = construct + "\t\tthis."+"narrow_"+dws.getName()+"_"+dws.getQos_dw().QoSabbrDw()+" = "+dws.getTopic().getName()+"DataWriterHelper.narrow("+dws.getName()+"_"+dws.getQos_dw().QoSabbrDw()+");\n";

		}
		for(Dr drs: a.getDr()) {
			if(drs.getQos_dr().isDefaultQoSdr()) {
				construct = construct+"\n\t\tthis."+drs.getName()+"_"+drs.getQos_dr().QoSabbrDr()+" = sub_"+drs.getSub().getDp()+".create_datareader("+drs.getTopic().getName()+"_"+drs.getSub().getDp()+" "
						+ ", DATAREADER_QOS_DEFAULT.get(), null, DEFAULT_STATUS_MASK.value);\n";
			}
			else {
				construct = construct+"\n\t\tthis."+drs.getName()+"_"+drs.getQos_dr().QoSabbrDr()+" = sub_"+drs.getSub().getDp()+".create_datareader("+drs.getTopic().getName()+"_"+drs.getSub().getDp()+" "
						+ ", "+ "get"+drs.getName()+"_"+drs.getQos_dr().QoSabbrDr()+"QoS(sub_"+drs.getSub().getDp()+"), null, DEFAULT_STATUS_MASK.value);\n";
			}

			construct = construct + "\t\tif("+drs.getName()+"_"+drs.getQos_dr().QoSabbrDr()+" == null) {\n\t\t\tSystem.out.println(\"Error DataReader creation\");\n\t\t\treturn;\n\t\t}\n";

		}
		return construct+"\n\t}";
	}

	private String printGetters(ArrayList<String> dps, ArrayList<Pub> pubs, ArrayList<Sub> subs, 
			ArrayList<Topic> topics, ArrayList<Dw> dw, ArrayList<Dr>dr, ArrayList<FilteredTopic> ftps) {
		String getters = "\n";

		for (String dp : dps) {
			getters = getters+"\tpublic DomainParticipant getDp_"+dp+"() {\n\t\treturn "+"dp_"+dp+";\n\t}\n";
		}
		for (Pub pub : pubs) {
			getters = getters+"\tpublic Publisher getPub_"+pub.getDp()+"() {\n\t\treturn "+pub.getName()+";\n\t}\n";
		}
		for (Sub sub : subs) {
			getters = getters+"\tpublic Subscriber getSub_"+sub.getDp()+"() {\n\t\treturn "+sub.getName()+";\n\t}\n";
		}
		for (Topic top : topics) {
			getters = getters+"\tpublic Topic getTopic"+top.getName()+"_"+top.getDp()+"() {\n\t\treturn "+top.getName()+"_"+top.getDp()+";\n\t}\n";
		}
		for (Dw d : dw) {
			getters = getters+"\tpublic DataWriter getDW"+d.getName()+"_"+d.getQos_dw().QoSabbrDw()+"() {\n\t\treturn "+d.getName()+"_"+d.getQos_dw().QoSabbrDw()+";\n\t}\n";
			getters = getters+"\tpublic "+d.getTopic().getName()+"DataWriter getNarrow_"+d.getName()+"_"+d.getQos_dw().QoSabbrDw()+"() {\n\t\treturn narrow_"+d.getName()+"_"+d.getQos_dw().QoSabbrDw()+";\n\t}\n";
		}
		for (Dr d : dr) {
			getters = getters+"\tpublic DataReader getDR"+d.getName()+"_"+d.getQos_dr().QoSabbrDr()+"() {\n\t\treturn "+d.getName()+"_"+d.getQos_dr().QoSabbrDr()+";\n\t}\n";
		}
		for (FilteredTopic tf : ftps) {
			int hashcode = Math.abs(tf.getFilterExpression().hashCode());
			getters = getters+"\tpublic DataReader getFiltered_"+tf.getDr().getName()+tf.getDr().getQos_dr().QoSabbrDr()+"_"+hashcode+"() {\n\t\treturn filtered_"+tf.getDr().getName()+tf.getDr().getQos_dr().QoSabbrDr()+"_"+hashcode+";\n\t}\n";;

		}
		getters = getters+"\n";
		return getters;
	}

	private String printTopicQos(Application a) {
		HashMap<String, Topic> topics = new HashMap<String, Topic>();
		String topicQos = "\n";

		for (Topic t : a.getTopic()) {
			if(!(topics.containsKey(t.getName())) && !t.getQos().isDefaultQoS()) {
				topics.put(t.getName(), t);
			}
		}
		for (Topic t2 : topics.values()) {
			topicQos = topicQos+"\tprivate TopicQos get"+t2.getName()+"QoS (DomainParticipant dp) {\n\t\tTopicQos top_qos = TOPIC_QOS_DEFAULT.get();\n\t\tTopicQosHolder top_holder = new TopicQosHolder(top_qos);\n\t\t"
					+ "dp.get_default_topic_qos(top_holder);\n\t\ttop_qos=top_holder.value;\n";
			topicQos = topicQos+setQoS(t2)+"\t\treturn top_qos;\n\t}\n";
		}


		return topicQos;
	}

	private String printDwQoS(Application a) {
		String dwqos = "\n";
		for (Dw d : a.getDw()) {
			if(!d.getQos_dw().isDefaultQoSdw()) {
				dwqos = dwqos+"\tprivate DataWriterQos get"+d.getName()+"_"+d.getQos_dw().QoSabbrDw()+"QoS(Publisher pub) {\n\t\tDataWriterQos dw_qos = DATAWRITER_QOS_DEFAULT.get();\n"
						+ "\t\tDataWriterQosHolder qos_holder = new DataWriterQosHolder(dw_qos);\n\t\tpub.get_default_datawriter_qos(qos_holder);\n\t\tdw_qos = qos_holder.value;\n";
				dwqos = dwqos+setQosDw(d)+"\t\treturn dw_qos;\n\t}\n";
			}
		}
		return dwqos;
	}
	private String printDrQos(Application a) {
		String drqos = "\n";
		for (Dr d : a.getDr()) {
			if(!d.getQos_dr().isDefaultQoSdr()) {
				drqos = drqos+"\tprivate DataReaderQos get"+d.getName()+"_"+d.getQos_dr().QoSabbrDr()+"QoS(Subscriber sub) {\n\t\tDataReaderQos dr_qos = DATAREADER_QOS_DEFAULT.get();\n"
						+ "\t\tDataReaderQosHolder qos_holder = new DataReaderQosHolder(dr_qos);\n\t\tsub.get_default_datareader_qos(qos_holder);\n\t\tdr_qos = qos_holder.value;\n";
				drqos = drqos+setQosDr(d)+"\t\treturn dr_qos;\n\t}\n";
			}
		}
		return drqos;
	}


	private String setQoS(Topic t) {

		String reliability = "\t\ttop_qos.reliability.kind = ReliabilityQosPolicyKind."+t.getQos().getReliability().toUpperCase()+"_RELIABILITY_QOS;\n";
		String durability = "\t\ttop_qos.durability.kind = DurabilityQosPolicyKind."+t.getQos().getDurability().toUpperCase()+"_DURABILITY_QOS;\n";
		String deadline = "\t\ttop_qos.deadline.period = new Duration_t("+t.getQos().getDeadline()+",0);\n";
		if (t.getQos().getDeadline().equals("INFINITY")) {
			deadline = "\t\ttop_qos.deadline.period.sec = DURATION_INFINITE_SEC.value;\n";
		}
		String ownership = "\t\ttop_qos.ownership.kind = OwnershipQosPolicyKind."+t.getQos().getOwnership().toUpperCase()+"_OWNERSHIP_QOS;\n";
		String liveliness = "\t\ttop_qos.liveliness.kind = LivelinessQosPolicyKind."+t.getQos().getLiveliness().toUpperCase()+"_LIVELINESS_QOS;\n";
		String livelinesslease = "\t\ttop_qos.liveliness.lease_duration = new Duration_t("+t.getQos().getLiveliness_lease_duration()+",0);\n";
		if(t.getQos().getLiveliness_lease_duration().equals("INFINITY")) {
			livelinesslease = "\t\ttop_qos.liveliness.lease_duration.sec = DURATION_INFINITE_SEC.value;\n";
		}
		String history = "\t\ttop_qos.history.kind = HistoryQosPolicyKind."+t.getQos().getHistory().toUpperCase()+"_HISTORY_QOS;\n";
		String historydepth = "\t\ttop_qos.history.depth = "+t.getQos().getHistory_depth()+";\n";


		String qos = reliability+durability+deadline+ownership+liveliness+livelinesslease+history+historydepth;;
		return qos;
	}
	private String setQosDw(Dw d) {
		String reliability = "\t\tdw_qos.reliability.kind = ReliabilityQosPolicyKind."+d.getQos_dw().getReliability().toUpperCase()+"_RELIABILITY_QOS;\n";
		String durability = "\t\tdw_qos.durability.kind = DurabilityQosPolicyKind."+d.getQos_dw().getDurability().toUpperCase()+"_DURABILITY_QOS;\n";
		String deadline = "\t\tdw_qos.deadline.period = new Duration_t("+d.getQos_dw().getDeadline()+",0);\n";
		if (d.getQos_dw().getDeadline().equals("INFINITY")) {
			deadline = "\t\tdw_qos.deadline.period.sec = DURATION_INFINITE_SEC.value;\n";
		}
		String ownership = "\t\tdw_qos.ownership.kind = OwnershipQosPolicyKind."+d.getQos_dw().getOwnership().toUpperCase()+"_OWNERSHIP_QOS;\n";
		String liveliness = "\t\tdw_qos.liveliness.kind = LivelinessQosPolicyKind."+d.getQos_dw().getLiveliness().toUpperCase()+"_LIVELINESS_QOS;\n";
		String livelinesslease = "\t\tdw_qos.liveliness.lease_duration = new Duration_t("+d.getQos_dw().getLiveliness_lease_duration()+",0);\n";
		if(d.getQos_dw().getLiveliness_lease_duration().equals("INFINITY")) {
			livelinesslease = "\t\tdw_qos.liveliness.lease_duration.sec = DURATION_INFINITE_SEC.value;\n";
		}
		String history = "\t\tdw_qos.history.kind = HistoryQosPolicyKind."+d.getQos_dw().getHistory().toUpperCase()+"_HISTORY_QOS;\n";
		String historydepth = "\t\tdw_qos.history.depth = "+d.getQos_dw().getHistory_depth()+";\n";
		String ownershipstr = "\t\tdw_qos.ownership_strength.value = "+d.getQos_dw().getOwnershipstrength()+";\n";;
		String qos = reliability+durability+deadline+ownership+ownershipstr+liveliness+livelinesslease+history+historydepth;;
		return qos;
	}

	private String setQosDr(Dr d) {
		String reliability = "\t\tdr_qos.reliability.kind = ReliabilityQosPolicyKind."+d.getQos_dr().getReliability().toUpperCase()+"_RELIABILITY_QOS;\n";
		String durability = "\t\tdr_qos.durability.kind = DurabilityQosPolicyKind."+d.getQos_dr().getDurability().toUpperCase()+"_DURABILITY_QOS;\n";
		String deadline = "\t\tdr_qos.deadline.period = new Duration_t("+d.getQos_dr().getDeadline()+",0);\n";
		if (d.getQos_dr().getDeadline().equals("INFINITY")) {
			deadline = "\t\tdr_qos.deadline.period.sec = DURATION_INFINITE_SEC.value;\n";
		}
		String ownership = "\t\tdr_qos.ownership.kind = OwnershipQosPolicyKind."+d.getQos_dr().getOwnership().toUpperCase()+"_OWNERSHIP_QOS;\n";
		String liveliness = "\t\tdr_qos.liveliness.kind = LivelinessQosPolicyKind."+d.getQos_dr().getLiveliness().toUpperCase()+"_LIVELINESS_QOS;\n";
		String livelinesslease = "\t\tdr_qos.liveliness.lease_duration = new Duration_t("+d.getQos_dr().getLiveliness_lease_duration()+",0);\n";
		if(d.getQos_dr().getLiveliness_lease_duration().equals("INFINITY")) {
			livelinesslease = "\t\tdr_qos.liveliness.lease_duration.sec = DURATION_INFINITE_SEC.value;\n";
		}
		String history = "\t\tdr_qos.history.kind = HistoryQosPolicyKind."+d.getQos_dr().getHistory().toUpperCase()+"_HISTORY_QOS;\n";
		String historydepth = "\t\tdr_qos.history.depth = "+d.getQos_dr().getHistory_depth()+";\n";
		String timefilter = "\t\tdr_qos.time_based_filter.minimum_separation = new Duration_t("+d.getQos_dr().getTimebasedfilter()+",0);\n";
		if(d.getQos_dr().getTimebasedfilter().equals("0")) {
			timefilter = "";
		}
		String qos = reliability+durability+deadline+ownership+liveliness+livelinesslease+history+historydepth+timefilter;
		return qos;
	}



}
