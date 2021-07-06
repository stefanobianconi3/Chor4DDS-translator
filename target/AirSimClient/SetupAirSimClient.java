package AirSimClient;

import DDS.*;
import OpenDDS.DCPS.*;
import Sample.*;

public class SetupAirSimClient {
	private DomainParticipant dp_0;
	private Publisher pub_0;


	public SetupAirSimClient(DomainParticipantFactory dpf) {

		//DOMAIN PARTICIPANTS CREATION
		this.dp_0 = dpf.create_participant(0, PARTICIPANT_QOS_DEFAULT.get(), null, DEFAULT_STATUS_MASK.value);
		if (dp_0 == null) {
			System.out.println ("Domain Participant creation failed");
			return;
		}

		//PUBLISHER AND SUBSCRIBER CREATION
		this.pub_0 = dp_0.create_publisher(PUBLISHER_QOS_DEFAULT.get(), null, DEFAULT_STATUS_MASK.value);


		//TOPIC REGISTRATION ON DOMAIN PARTICIPANT


		//TOPIC CREATION

		//DATAREADER AND DATAWRITER CREATION

	}
	public DomainParticipant getDp_0() {
		return dp_0;
	}
	public Publisher getPub_0() {
		return pub_0;
	}





}