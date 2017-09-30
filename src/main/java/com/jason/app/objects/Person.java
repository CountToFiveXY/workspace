package com.jason.app.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Person {
	private String name;
	@Setter private double salary;
	private boolean fullTime;
	
	public Person(String name) {
		this.name = name;
		this.salary = 0;
		this.fullTime = false;
	}
}
