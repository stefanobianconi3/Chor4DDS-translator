package DashboardClient;

import DDS.*;
import OpenDDS.DCPS.*;
import Sample.*;

public class SetupDashboardClient {
	private DomainParticipant dp_0;
	private Publisher pub_0;
	private Subscriber sub_0;


	public SetupDashboardClient(DomainParticipantFactory dpf) {

		//DOMAIN PARTICIPANTS CREATION
		this.dp_0 = dpf.create_participant(0, PARTICIPANT_QOS_DEFAULT.get(), null, DEFAULT_STATUS_MASK.value);
		if (dp_0 == null) {
			System.out.println ("Domain Participant creation failed");
			return;
		}

		//PUBLISHER AND SUBSCRIBER CREATION
		this.pub_0 = dp_0.create_publisher(PUBLISHER_QOS_DEFAULT.get(), null, DEFAULT_STATUS_MASK.value);

		this.sub_0 = dp_0.create_subscriber(SUBSCRIBER_QOS_DEFAULT.get(), null, DEFAULT_STATUS_MASK.value);

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
	public Subscriber getSub_0() {
		return sub_0;
	}





}