package ddsApplication;

import java.util.ArrayList;

public class FilteredTopic {
	private Topic topic;
	private Dr dr;
	private String filterExpression;

	public FilteredTopic(Topic topic, String filterExpression, Dr dr) {
		this.filterExpression = filterExpression;
		this.dr=dr;
	}

	public Dr getDr() {
		return dr;
	}

	public void setDr(Dr dr) {
		this.dr = dr;
	}

	public String getFilterExpression() {
		return filterExpression;
	}
	public void setFilterExpression(String filterExpression) {
		this.filterExpression = filterExpression;
	}

	public ArrayList<String> getPlaceholder(){

		String numbersLine = filterExpression.replaceAll("([^0-9]+[^.][^0-9]+)", " ");
		String[] strArray = numbersLine.split(" ");
		ArrayList<String> mylist = new ArrayList<String>();

		for (String string : strArray) {
			if (!string.equals("")) {
				mylist.add(string);
			}
		}
		return mylist;
	}

	public String getFilterModified() {
		String fmod = getFilterExpression().replaceAll("(=|>|<|>=|<|<=|<>|!=|=<|=>)\\s\\d+", "$1% ");
		fmod = fmod.replaceAll("(=|>|<|>=|<|<=|<>|!=|=<|=>)\\d+", "$1% ");
		for (int i = 0; i < getPlaceholder().size(); i++) {
			Integer in = i;
			fmod = fmod.replaceFirst("(%)\\D", "$1"+in.toString()+" ");
		}



		return fmod;

	}
	public Topic getTopic() {
		return topic;
	}

}
