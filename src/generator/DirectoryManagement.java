package generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import configuration.Directory;
import ddsApplication.Application;

public class DirectoryManagement {
	private Directory dir;
	public DirectoryManagement(Directory d) {
		this.dir=d;
	}

	
//	protected void commandExec() {
//		try {
//			String envar = "var="+dir.getIDLC();
//			String [] env = new String[]{envar};
//			Process p0 = Runtime.getRuntime().exec("cmd /c setenv.cmd", null, new File("C:\\OpenDDS-3.16"));
//			p0.waitFor();
//			Process p = Runtime.getRuntime().exec("cmd /c %ACE_ROOT%\\bin\\generate_export_file.pl %var% > %var%_Export.h", env, new File(dir.getPATH_IDL()));
//			p.waitFor();
//			Process p2 = Runtime.getRuntime().exec("cmd /c %ACE_ROOT%\\bin\\mwc.pl -type vs2019", null, new File(dir.getPATH_IDL()));
//			p2.waitFor();
//		} catch (Exception e) {
//			System.out.println("Error on terminal commands");
//		}
//	}
	
	/**
	 * Crate target folder and idl sub-folder 
	 */
	protected void createTarget() {
		try {
			Path targetPath = Paths.get(dir.getPATH_OUTPUT());
			Path idlPath = Paths.get(dir.getPATH_IDL());
			


//			if(Files.exists(targetPath)) {
//				deleteDirectoryStream(targetPath);
//			}

			Files.createDirectories(targetPath);
			System.out.println("created "+dir.getPATH_OUTPUT());
			Files.createDirectories(idlPath);
			System.out.println("created "+dir.getPATH_IDL());

		} catch (IOException e) {
			System.err.println("TARGET FOLDER CREATION FAILED #########" + e.getMessage());
		}
	}

	/**
	 * Delete the output folder and all its content
	 * @param path
	 * @throws IOException
	 */
	private void deleteDirectoryStream(Path path) throws IOException {
		Files.walk(path)
		.sorted(Comparator.reverseOrder())
		.map(Path::toFile)
		.forEach(File::delete);
	}

	/**
	 * It generats the file with the name specified in Directory
	 * @param f: the String containing the overall idl file.
	 */
	protected void idlGenerator(String content) {
		//path = target/{IDL}/{IDL}.idl
		Path path = Paths.get(dir.getPATH_IDL_FILE());
	

		try (BufferedWriter writer =
				Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

			writer.write(content);
			System.out.println("IDL file generated at: "+ path);

		} catch (IOException e) {
			System.out.println("IDL WRITING FAILED #########");
			e.printStackTrace();
		}
	}
	
	protected void mpcGenerator(String content) {
		//path = target/{IDL}/{IDL}.idl
		Path path = Paths.get(dir.getPATH_MPC_FILE());
	

		try (BufferedWriter writer =
				Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

			writer.write(content);
			System.out.println("MPC file generated at: "+ path);

		} catch (IOException e) {
			System.out.println("MPC WRITING FAILED #########");
			e.printStackTrace();
		}
	}

	protected void foldersGenerator(Application application) {
		try {
			Path targetPath = Paths.get(dir.getPATH_OUTPUT());

			if(Files.exists(targetPath)) {
				
					String name = application.getName();
					String folder = dir.getPATH_OUTPUT()+name+File.separator;
					Path folderPath = Paths.get(folder);
					Files.createDirectories(folderPath);
					System.out.println("Application folder generated at: "+folderPath);
				

			}


		} catch (IOException e) {
			System.err.println("APPLICATIONS FOLDER CREATION FAILED #########" + e.getMessage());
		}
	}

	protected void classFileGenerator(Application a) {
		File setup = new File(a.getPathFile()+"Setup"+a.getName()+".java");
		File main = new File(a.getPathFile()+"Main"+a.getName()+".java");
		try   
		{  
			setup.createNewFile();
			main.createNewFile();
		}catch (IOException e)   
		{  
			e.printStackTrace();    
		}         
	}
}
