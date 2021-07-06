package AirSimClient;

import org.omg.CORBA.StringSeqHolder;
import OpenDDS.DCPS.*;
import DDS.*;
import Sample.*;

public class MainAirSimClient {
	 public static void main(String[] args) {
		SetupAirSimClient setup = new SetupAirSimClient(TheParticipantFactory.WithArgs(new StringSeqHolder(args)));

	}
}