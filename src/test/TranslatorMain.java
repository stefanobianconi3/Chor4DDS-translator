package test;


import java.util.HashMap;

import configuration.Directory;
import converter.ModelConverted;
import ddsApplication.Application;
import generator.ClassGenerator;
import generator.CodeGenerator;
import generator.IdlGenerator;

public class TranslatorMain {


	public static void main(String[] args) {
		Directory dir = new Directory("target", "thermo");
		
		ModelConverted modelConverted = new ModelConverted();
		modelConverted.convertAll("res/example.bpmn");
		
		IdlGenerator idlGen = new IdlGenerator(modelConverted,dir);
		idlGen.generateIdl();

		ClassGenerator classGen = new ClassGenerator(modelConverted, dir);
		HashMap<String, Application> applications = classGen.getApplications();
		

	
		
		CodeGenerator codegen = new CodeGenerator(applications, dir);
		codegen.writeCode();
		
		
	}

	/**
	 * Test the output of the conversion
	 * @param ap
	 */
//	private static void testPrint(HashMap<String, Application> ap) {
//		for (String key : ap.keySet()) {
//			
//			System.out.println("APPLICATION NAME : " +ap.get(key).getName());
//			System.out.println("path: " +ap.get(key).getPathFile());
//			System.out.println("ID IS "+key);
//			System.out.println("pub size: " +ap.get(key).getPublisher().size());
//			for (Dw dw : ap.get(key).getDw()) {
//				System.out.println("dwName: "+dw.getName() +" || PUB dp:"+dw.getPub().getDp()+" name: "
//						+dw.getPub().getName()+" || TOPIC name:"+dw.getTopic().getName());
//			}
//			System.out.println(".........................");
//			System.out.println("sub size: " +ap.get(key).getSubscriber().size());
//			for (Dr dr : ap.get(key).getDr()) {
//				System.out.println("NAME: "+dr.getName() +" || SUB dp:"+dr.getSub().getDp()+" name: "
//						+dr.getSub().getName()+" || TOPIC name:"+dr.getTopic().getName());
//			}
//
//			for (Topic t : ap.get(key).getTopic()) {
//				System.out.println("Topic name "+t.getName()+" dp "+t.getDp());
//			}
//			System.out.println("------------------------------------");
//		}
//	}




}
