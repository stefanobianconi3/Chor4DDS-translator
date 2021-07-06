package bpmnElement;

/**
 * Quality of Service attached to a task, similar to QoS class but adds two additional QoS (ownership strength and time based filter)
 * Used to set the QoS for both DataWriter and DataReader
 * @author Stefano Bianconi
 *
 */
public class QosTask extends QoS{
	

	private String ownershipstrength;
	private String timebasedfilter;
	
	public QosTask(String reliability, String durability, String deadline, String ownership, String liveliness,
			String liveliness_lease_duration, String history, String history_depth,String ownershipstrenght, String timebasedfilter) {
		super(reliability, durability, deadline, ownership, liveliness, liveliness_lease_duration, history, history_depth);
		this.ownershipstrength = ownershipstrenght;
		this.timebasedfilter = timebasedfilter;
		
	}

	public String getOwnershipstrength() {
		return ownershipstrength;
	}

	public void setOwnershipstrength(String ownershipstrength) {
		this.ownershipstrength = ownershipstrength;
	}

	public String getTimebasedfilter() {
		return timebasedfilter;
	}

	public void setTimebasedfilter(String timebasedfilter) {
		this.timebasedfilter = timebasedfilter;
	}

	public boolean isDefaultQoSdw() {
		if(getReliability().equals("best_effort") && getDurability().equals("volatile") && getDeadline().equals("INFINITY") &&
				getOwnership().equals("shared") && getLiveliness().equals("automatic") && getLiveliness_lease_duration().equals("INFINITY") &&
				getHistory().equals("keep_last") &&	getHistory_depth().equals("1") && this.ownershipstrength.equals("0")) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean isDefaultQoSdr() {
		if(getReliability().equals("best_effort") && getDurability().equals("volatile") && getDeadline().equals("INFINITY") &&
				getOwnership().equals("shared") && getLiveliness().equals("automatic") && getLiveliness_lease_duration().equals("INFINITY") &&
				getHistory().equals("keep_last") &&	getHistory_depth().equals("1") && this.timebasedfilter.equals("0")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String stringQoSdr() {
		return getReliability()+getDurability()+getDeadline()+getOwnership()+getLiveliness()+getLiveliness_lease_duration()+
				getHistory()+getHistory_depth()+getTimebasedfilter();
	}
	public String stringQoSdw() {
		return getReliability()+getDurability()+getDeadline()+getOwnership()+getLiveliness()+getLiveliness_lease_duration()+
				getHistory()+getHistory_depth()+getOwnershipstrength();
	}
	
	public String QoSabbrDw() {
		String str = "";
		return str+getReliability().charAt(0)+getDurability().charAt(1)+getDeadline().charAt(0)+getOwnership().charAt(0)+
				getLiveliness().charAt(0)+getLiveliness_lease_duration().charAt(0)+getHistory().charAt(5)+
				getOwnershipstrength().charAt(0);
	}
	public String QoSabbrDr() {
		String str = "";
		return str+getReliability().charAt(0)+getDurability().charAt(1)+getDeadline().charAt(0)+getOwnership().charAt(0)+
				getLiveliness().charAt(0)+getLiveliness_lease_duration().charAt(0)+getHistory().charAt(5)+
				getOwnershipstrength().charAt(0);
	}

}
