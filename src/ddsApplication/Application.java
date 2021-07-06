package ddsApplication;

import java.util.ArrayList;


/**
 * Representation of DDS application being generated. This class act as entry point of the translator which produce java code
 * @author Stefano Bianconi
 *
 */
public class Application {
	private String id;
	private String name;
	private String pathFile;
	private ArrayList<String> dps;
	private ArrayList<Pub> publisher;
	private ArrayList<Sub> subscriber;
	private ArrayList<Topic> topic;
	private ArrayList<Dw> dw;
	private ArrayList<Dr> dr;
	private ArrayList<FilteredTopic> filteredTopics;


	public Application(String id,String name, String pathFile, ArrayList<String> dps, ArrayList<Pub> publisher,
			ArrayList<Sub> subscriber, ArrayList<Topic> topic, ArrayList<Dw> dw, ArrayList<Dr> dr, ArrayList<FilteredTopic> filteredTopics) {
		super();
		this.id=id;
		this.name = name;
		this.pathFile = pathFile;
		this.dps = dps;
		this.publisher = publisher;
		this.subscriber = subscriber;
		this.topic = topic;
		this.dw = dw;
		this.dr = dr;
		this.filteredTopics = filteredTopics;
	}

	public ArrayList<FilteredTopic> getFilteredTopics() {
		return filteredTopics;
	}

	public void addFilteredTopics(FilteredTopic ft) {
		for (FilteredTopic topics : this.filteredTopics) {
			if(topics.getDr().getName().equals(ft.getDr().getName()) && topics.getFilterExpression().equals(ft.getFilterExpression()) ) {
				return;
			}
		}
		this.filteredTopics.add(ft);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPathFile() {
		return pathFile;
	}
	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}
	public ArrayList<String> getDps() {
		return dps;
	}

	public void addDps(String dp) {
		if(this.dps.contains(dp)) {
			return;
		}
		else {
			this.dps.add(dp);
		}
	}
	public ArrayList<Pub> getPublisher() {
		return publisher;
	}

	/**
	 * add the publisher on the arrayList only if the name is different. Each publisher has name: pub_[DP]
	 * @param pub
	 */
	public void addPub(Pub pub) {
		for (Pub p : this.publisher) {
			if(p.getName().equals(pub.getName())) {
				return;
			}
		}
		this.publisher.add(pub);

	}

	public ArrayList<Sub> getSubscriber() {
		return subscriber;
	}
	
	public void addSub(Sub sub) {
		for (Sub s : this.subscriber) {
			if(s.getName().equals(sub.getName())) {
				return;
			}
		}
		this.subscriber.add(sub);
	}
	
	public ArrayList<Topic> getTopic() {
		return topic;
	}
	
	public void addTopic(Topic topic) {
		for (Topic t : this.topic) {
			if(t.getName().equals(topic.getName()) && t.getDp().equals(topic.getDp())) {
				return;
			}
		}
		this.topic.add(topic);
	}
	
	public ArrayList<Dw> getDw() {
		return dw;
	}
	
	public void addDw(Dw dws) {
		for (Dw d : this.dw) {

			if(d.getName().equals(dws.getName()) && d.getQos_dw().stringQoSdw().equals(dws.getQos_dw().stringQoSdw())) {
				return;
			}
		}
		this.dw.add(dws);

	}
	public ArrayList<Dr> getDr() {
		return dr;
	}
	
	
	public void addDr(Dr drs) {
		for (Dr d : this.dr) {
			
			if(d.getName().equals(drs.getName()) && d.getQos_dr().stringQoSdr().equals(drs.getQos_dr().stringQoSdr())) {
				return;
			}
		}
		
		this.dr.add(drs);

	}
	
//	public void addDr(Dr drs) {
//		for (Dr d : this.dr) {
//			if(d.getName().equals(drs.getName()) && d.getQos_dr().stringQoSdr().equals(drs.getQos_dr().stringQoSdr())) {
//				if(d.getFilterTopic()== null && drs.getFilterTopic() == null) {
//					return;
//				}
//				if (d.getFilterTopic()!= null && drs.getFilterTopic() != null) {
//					if (d.getFilterTopic().equals(drs.getFilterTopic())) {
//						return;
//					}
//					else {
//						this.dr.add(drs);
//						return;
//					}
//				}
//				if(d.getFilterTopic()!= null ^ drs.getFilterTopic()!= null ) {
//					this.dr.add(drs);
//					return;
//				}
//			}
//		}
//		this.dr.add(drs);


	}



