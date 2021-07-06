package BrakeModule;

import org.omg.CORBA.StringSeqHolder;
import OpenDDS.DCPS.*;
import DDS.*;
import Sample.*;

public class MainBrakeModule {
	 public static void main(String[] args) {
		SetupBrakeModule setup = new SetupBrakeModule(TheParticipantFactory.WithArgs(new StringSeqHolder(args)));

	}
}