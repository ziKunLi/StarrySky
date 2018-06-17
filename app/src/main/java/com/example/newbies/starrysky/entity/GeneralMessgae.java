package com.example.newbies.starrysky.entity;

import java.util.List;

/**
 * @author NewBies
 */
public class GeneralMessgae {
	
	private List<String> peopleList;
	
	private String message;

	public List<String> getPeopleList() {
		return peopleList;
	}

	public String getMessage() {
		return message;
	}

	public void setPeopleList(List<String> peopleList) {
		this.peopleList = peopleList;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
