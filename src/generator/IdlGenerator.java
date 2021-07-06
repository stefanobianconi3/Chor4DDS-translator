package generator;

import configuration.Directory;
import configuration.IdlPrinter;
import converter.ModelConverted;

public class IdlGenerator {
	private DirectoryManagement dm;
	private ModelConverted modelConverted;
	private Directory dir;
	private IdlPrinter idlPrinter;
	
	public IdlGenerator(ModelConverted modelConverted, Directory d) {
		this.dir=d;
		this.dm = new DirectoryManagement(d);
		this.idlPrinter = new IdlPrinter(d);
		this.modelConverted = modelConverted;
		
	}
	
	/**
	 * Creates the output folder, the idl folder and the IDL file
	
	 */
	public void generateIdl() {
		dm.createTarget();
		String body = idlPrinter.idlTemplate(modelConverted.getTopics());
		dm.idlGenerator(body);
		dm.mpcGenerator(idlPrinter.mpcTemplate(dir.getIDLC()));
		//dm.commandExec();
	}
}
