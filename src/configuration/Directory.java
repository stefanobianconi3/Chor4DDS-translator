package configuration;

import java.io.File;

public class Directory {
	private String PATH_OUTPUT;
	private String IDL;
	private String IDLC;
	private String PATH_IDL;
	private String PATH_IDL_FILE;
	private String PATH_MPC_FILE;
	
	public Directory (String target, String name){
		/**
		 * Path and Name of the output folder being generated
		 */
		this.PATH_OUTPUT = target+File.separator;
		/**
		 * Name of the IDL folder, IDL file and IDL module
		 */
		this.IDL = name;
		this.IDLC = IDL.substring(0, 1).toUpperCase() + IDL.substring(1);
		this.PATH_IDL = PATH_OUTPUT+IDL+File.separator;
		this.PATH_IDL_FILE = PATH_IDL+IDLC+".idl";
		this.PATH_MPC_FILE = PATH_IDL+IDLC+".mpc";
	}

	public String getPATH_OUTPUT() {
		return PATH_OUTPUT;
	}

	public String getIDL() {
		return IDL;
	}

	public String getIDLC() {
		return IDLC;
	}

	public String getPATH_IDL() {
		return PATH_IDL;
	}

	public String getPATH_IDL_FILE() {
		return PATH_IDL_FILE;
	}

	public String getPATH_MPC_FILE() {
		return PATH_MPC_FILE;
	}

	

}
