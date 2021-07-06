package test;

import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import configuration.Directory;
import converter.ModelConverted;
import ddsApplication.Application;
import generator.ClassGenerator;
import generator.CodeGenerator;
import generator.IdlGenerator;

public class mainApp {
	public static void main(String[] args) {

		ModelConverted modelConverted = new ModelConverted();

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("SELECT THE INPUT BPMN FILE");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("xml", "bpmn");
		chooser.setFileFilter(filter);
		
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			modelConverted.convertAll(chooser.getSelectedFile().getPath());

			
			JFileChooser chooserout = new JFileChooser();
			chooserout.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooserout.setDialogTitle("CHOOSE THE OUTPUT FOLDER");
			int returnVal2 = chooserout.showOpenDialog(null);
			if(returnVal2 == JFileChooser.APPROVE_OPTION) {
				
				Directory dir = new Directory(chooserout.getSelectedFile().getPath(), modelConverted.getIdlName());
				IdlGenerator idlGen = new IdlGenerator(modelConverted,dir);
				idlGen.generateIdl();

				ClassGenerator classGen = new ClassGenerator(modelConverted, dir);
				HashMap<String, Application> applications = classGen.getApplications();


				CodeGenerator codegen = new CodeGenerator(applications, dir);
				codegen.writeCode();

			}
		}


	}
}
