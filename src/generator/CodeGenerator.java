package generator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import configuration.Directory;
import configuration.MainClassPrinter;
import configuration.SetupPrinter;
import configuration.ListenerPrinter;
import ddsApplication.Application;
import ddsApplication.Dr;
import ddsApplication.Topic;

public class CodeGenerator {
	private DirectoryManagement dm;
	private ArrayList<Application> applications;
	private Directory dir;
	
	public CodeGenerator(HashMap<String, Application> apps, Directory d) {
		this.dm = new DirectoryManagement(d);
		this.applications = convertMap(apps);
		this.dir = d;
	

	}

	public void writeCode() {

		for (Application application : applications) {
			if(application.getPublisher().size() > 0 || application.getSubscriber().size()>0) {
				setFolders(application);
				writeSetup(application);
				writeMain(application);
				writeListeners(application);
			}
		}
	}
	
	private void writeListeners(Application a) {
		for (Topic t : getTopicListener(a)) {
			ListenerPrinter plist = new ListenerPrinter(a, t, dir);
			Path path = Paths.get(a.getPathFile()+t.getName()+"Listener.java");
			try (BufferedWriter writer =
					Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

				writer.write(plist.listenercode());
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
	
	private ArrayList<Topic> getTopicListener(Application a){
		ArrayList<Topic> result = new ArrayList<Topic>();
		for (Topic t : a.getTopic()) {
			for (Dr dr : a.getDr()) {
				if(dr.getTopic().getName().equals(t.getName()) && !result.contains(t)) {
					result.add(t);
				}
			}	
		}
		return result;
	}
	

	private void writeSetup(Application a) {
		SetupPrinter printsetupt = new SetupPrinter(a, dir); 
		Path path = Paths.get(a.getPathFile()+"Setup"+a.getName()+".java");

		try (BufferedWriter writer =
				Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

			writer.write(printsetupt.getSetupCode());
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	private void writeMain(Application a) {
		MainClassPrinter mainPrinter = new MainClassPrinter(a, dir);
		Path path = Paths.get(a.getPathFile()+"Main"+a.getName()+".java");
		try (BufferedWriter writer =
				Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

			writer.write(mainPrinter.mainCode());
		} catch (IOException e) {

			e.printStackTrace();
		}
	}



	private void setFolders(Application a) {

		dm.foldersGenerator(a);
		dm.classFileGenerator(a);

	}


	private ArrayList<Application> convertMap(HashMap<String, Application> apps){
		Collection<Application> values = apps.values();
		ArrayList<Application> applications = new ArrayList<>(values);
		return applications;
	}
}
