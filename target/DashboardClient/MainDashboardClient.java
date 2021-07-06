package DashboardClient;

import org.omg.CORBA.StringSeqHolder;
import OpenDDS.DCPS.*;
import DDS.*;
import Sample.*;

public class MainDashboardClient {
	 public static void main(String[] args) {
		SetupDashboardClient setup = new SetupDashboardClient(TheParticipantFactory.WithArgs(new StringSeqHolder(args)));

	}
}