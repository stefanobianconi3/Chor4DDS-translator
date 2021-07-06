package CollisionAvoidanceSystem;

import org.omg.CORBA.StringSeqHolder;
import OpenDDS.DCPS.*;
import DDS.*;
import Sample.*;

public class MainCollisionAvoidanceSystem {
	 public static void main(String[] args) {
		SetupCollisionAvoidanceSystem setup = new SetupCollisionAvoidanceSystem(TheParticipantFactory.WithArgs(new StringSeqHolder(args)));

	}
}