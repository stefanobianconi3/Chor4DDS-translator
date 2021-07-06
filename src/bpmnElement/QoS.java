package bpmnElement;

/**
 * This class represent the Quality of Services of the Topic (Message) 
 * @author Stefano Bianconi
 *
 */
public class QoS {

	private String reliability;
	private String durability;
	private String deadline;
	private String ownership;
	private String liveliness;
	private String liveliness_lease_duration;
	private String history;
	private String history_depth;



	public QoS(String reliability, String durability, String deadline, String ownership, String liveliness,
			String liveliness_lease_duration, String history, String history_depth) {
		this.reliability = reliability;
		this.durability = durability;
		this.deadline = deadline;
		this.ownership = ownership;
		this.liveliness = liveliness;
		this.liveliness_lease_duration = liveliness_lease_duration;
		this.history = history;
		this.history_depth = history_depth;
	}



	/**
	 * Check if the quality of service are the default in DDS
	 * @return true when the paremters are the default from the chor4dds modeler
	 */
	public boolean isDefaultQoS() {
		if(this.reliability.equals("best_effort") && this.durability.equals("volatile") && this.deadline.equals("INFINITY") &&
				this.ownership.equals("shared") && this.liveliness.equals("automatic") && this.liveliness_lease_duration.equals("INFINITY") &&
				this.history.equals("keep_last") &&	this.history_depth.equals("1")) {
			return true;
		}
		else {
			return false;
		}
	}

	public String getReliability() {
		return reliability;
	}

	public String getDurability() {
		return durability;
	}

	public String getDeadline() {
		return deadline;
	}

	public String getOwnership() {
		return ownership;
	}

	public String getLiveliness() {
		return liveliness;
	}

	public String getLiveliness_lease_duration() {
		return liveliness_lease_duration;
	}

	public String getHistory() {
		return history;
	}

	public String getHistory_depth() {
		return history_depth;
	}

	@Override
	public String toString() {
		return "QoS [reliability=" + reliability + ", durability=" + durability + ", deadline=" + deadline
				+ ", ownership=" + ownership + ", liveliness=" + liveliness + ", liveliness_lease_duration="
				+ liveliness_lease_duration + ", history=" + history + ", history_depth=" + history_depth + "]";
	}



}
