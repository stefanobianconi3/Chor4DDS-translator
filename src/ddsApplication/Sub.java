package ddsApplication;

/**
 * Representation of Subscriber
 * @author Stefano Bianconi
 *
 */
public class Sub {
	private String dp;
	private String name;

	public Sub(String dp, String name) {
		super();
		this.dp = dp;
		this.name = name;
	}
	public String getDp() {
		return dp;
	}
	public void setDp(String dp) {
		this.dp = dp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
