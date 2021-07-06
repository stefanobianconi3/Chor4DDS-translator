package generator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import bpmnElement.Message;
import bpmnElement.MessageFlow;
import bpmnElement.Participant;
import bpmnElement.Task;
import configuration.Directory;
import converter.ModelConverted;
import ddsApplication.Application;
import ddsApplication.Dr;
import ddsApplication.Dw;
import ddsApplication.FilteredTopic;
import ddsApplication.Pub;
import ddsApplication.Sub;
import ddsApplication.Topic;

/**
 * Class which receives the model converted and generate one Application object for each participant. 
 * The result is an HashMap with key: the id of the participant, and the value: the Application class containing the dds entities to be generated
 * @author Stefano Bianconi
 *
 */
public class ClassGenerator {
	private ModelConverted modelConverted;
	private HashMap<String, Application> applications;
	private Directory dir;

	public ClassGenerator(ModelConverted modelConverted, Directory d) {
		this.dir=d;
		this.modelConverted = modelConverted;
		this.applications = GenerateClasses();

	}

	public HashMap<String, Application> getApplications() {
		return applications;
	}

	private HashMap<String, Application> GenerateClasses() {

		HashMap<String, Application> result = setApplications(modelConverted.getParticipants());
		generateApplications(result, modelConverted.getTasks(), modelConverted.getTopics(), modelConverted.getMessageFlows());

		return result;
	}



	/**
	 * Generate an hashmap with all the participants: key is the id values is the Application object. This is just the initialization of the Hashmap
	 * @param participants
	 * @return HashMap<String, Application>
	 */
	private HashMap<String, Application> setApplications(ArrayList<Participant> participants) {
		HashMap<String, Application> result = new HashMap<String, Application>();
		for (Participant p : participants) {
			ArrayList<Pub> pub = new ArrayList<Pub>();
			ArrayList<Sub> sub = new ArrayList<Sub>();
			ArrayList<String> dps = new ArrayList<String>();
			ArrayList<Topic> topic = new ArrayList<Topic>();
			ArrayList<Dr> dr = new ArrayList<Dr>();
			ArrayList<Dw> dw = new ArrayList<Dw>();
			ArrayList<FilteredTopic> filteredTopics = new ArrayList<FilteredTopic>();
			Application app = new Application(p.getId(), p.getName(), dir.getPATH_OUTPUT()+p.getName()+File.separator, dps, pub, sub, topic, dw, dr, filteredTopics);
			result.put(p.getId(), app);
		}
		return result;
	}

	/**
	 * Populate the Object Application with all the attributes. Iterates all the tasks and update the reference on the HashMap
	 * @param apps: before created with the method setApplications. 
	 * @param task: from the model converted
	 * @param messages: from the model converted
	 */
	private void generateApplications(HashMap<String,Application> apps, ArrayList<Task> task, ArrayList<Message> messages, ArrayList<MessageFlow> mflows){
		//iterate all the tasks
		for (Task t : task) {

			//setting of publisher and data writer
			for (String source : t.getSources()) {
				Application sender = apps.get(source);
				Pub pub = new Pub(t.getDp(), "pub_"+t.getDp());
				sender.addDps(t.getDp());
				sender.addPub(pub);
				Message dwm = getMessageDw(t, messages, source);
				if(dwm != null) {
					Topic top = new Topic(dwm.getId(), dwm.getName(), dwm.getDocumentation(), dwm.getQos(), dwm.getFields(), t.getDp());

					sender.addTopic(top);

					Dw dw = new Dw(pub, t.getQos(), "dw_"+dwm.getName()+"_"+t.getDp(), dwm);
					sender.addDw(dw);
				}
			}

			//setting of subscriber and datareader
			for (String target : t.getTargets()) {
				Application receiver = apps.get(target);
				System.out.println(receiver.getId());
				Sub sub = new Sub(t.getDp(), "sub_"+t.getDp());
				receiver.addDps(t.getDp());
				receiver.addSub(sub);
				Message drm = getMessageDr(t, messages, target);

				if(drm != null ) {
					Topic topi = new Topic(drm.getId(), drm.getName(), drm.getDocumentation(), drm.getQos(), drm.getFields(), t.getDp());

					receiver.addTopic(topi);

					Dr dr = new Dr(sub, t.getQos(), "dr_"+drm.getName()+"_"+t.getDp(), drm);
					receiver.addDr(dr);
				}
			}

			setFilterExpression(apps, t, messages, mflows);

		}

	}



	/**
	 * @param t: Task
	 * @param messages: All the messages (topic)
	 * @return Message of the DataWriter (initiating of the task)
	 */
	private Message getMessageDw(Task t, ArrayList<Message> messages, String source) {
		for (MessageFlow mflow : t.getMessageFlow()) {
			for (Message message : messages) {
				if (mflow.getMessageRef().equals(message.getId()) && mflow.getSource().equals(source)){
					return message;
				}
			}

		}
		return null;
	}

	/**
	 * @param t: Task
	 * @param messages: All the messages (topic)
	 * @return Message of the DataReader (receiver of the task)
	 */
	private Message getMessageDr(Task t, ArrayList<Message> messages, String target) {
		for (MessageFlow mflow : t.getMessageFlow()) {
			for (Message message : messages) {
				if (mflow.getMessageRef().equals(message.getId()) && mflow.getTarget().equals(target)){
					return message;
				}
			}

		}
		return null;
	}

	/**
	 * Set the filter expression to the right DataReader
	 * @param t: Task
	 * @param dr: DataReader to set the filter.
	 * @param target to the right message.
	 * @return
	 */
	private void setFilterExpression(HashMap<String,Application> apps, Task t, ArrayList<Message> messages, ArrayList<MessageFlow> mflows) {

		for (MessageFlow messageFlow : t.getMessageFlow()) {
			Application receiver = apps.get(messageFlow.getTarget());
			String filter = messageFlow.getFilterExpression();
			if(!filter.equals("")) {
				Sub sub = new Sub(t.getDp(), "sub_"+t.getDp());
				Message drm = getMessageDr(t, messages, messageFlow.getTarget());
				if(drm != null ) {
					Topic topi = new Topic(drm.getId(), drm.getName(), drm.getDocumentation(), drm.getQos(), drm.getFields(), t.getDp());
					Dr d = new Dr(sub, t.getQos(), "dr_"+drm.getName()+"_"+t.getDp(), topi);
					FilteredTopic filtertopic = new FilteredTopic(topi, filter, d);
					receiver.addFilteredTopics(filtertopic);
				}
			}
		}





	}

}
