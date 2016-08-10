package com.example;

import java.io.Serializable;

public class SimpleForm implements Serializable{

	private static final long serialVersionUID = -2599623005036316521L;

	private String name;
	private String hobby;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	
	
}
